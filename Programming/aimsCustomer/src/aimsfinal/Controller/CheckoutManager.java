package aimsfinal.Controller;

import aimsfinal.DAO.DatabaseConnection;
import aimsfinal.GUI.CustomerFrame;
import aimsfinal.GUI.dialog.CartDialog;
import aimsfinal.GUI.dialog.PlaceOrderDialog;
import aimsfinal.Model.CartItem;
import aimsfinal.Model.DeliveryInfo;
import aimsfinal.Model.Order;
import aimsfinal.Model.OrderSummary;
import aimsfinal.Model.User;
import aimsfinal.Util.PaymentStatusListener;
import aimsfinal.Util.VnpayConfig;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class CheckoutManager implements PaymentStatusListener {

    private final CustomerFrame owner;
    private final User currentUser;
    private CartDialog cartDialog;
    private PlaceOrderDialog placeOrderDialog;

    // Controllers for database operations
    private final OrderController orderController;
    private final ProductController productController;
    private final CartController cartController;

    public CheckoutManager(CustomerFrame owner, User currentUser) {
        this.owner = owner;
        this.currentUser = currentUser;

        // We will create new controller instances that share a single transaction connection
        this.orderController = new OrderController();
        this.productController = new ProductController();
        this.cartController = new CartController();
    }

    // The main entry point to start the checkout process, called from CustomerShoppingPanel
    public void startCheckout() {
        if (cartDialog == null || !cartDialog.isDisplayable()) {
            cartDialog = new CartDialog(owner, this);
        }
        cartDialog.setVisible(true);
    }

    // Called from CartDialog, this method opens the dialog to collect delivery info.
    public void proceedToPayment(boolean isRush) {
        if (placeOrderDialog == null || !placeOrderDialog.isDisplayable()) {
            placeOrderDialog = new PlaceOrderDialog(owner, this, isRush);
        }
        placeOrderDialog.setVisible(true);
    }

    /**
     * Called by PlaceOrderDialog whenever a delivery option changes,
     * so we can recalculate and update the displayed costs.
     */
    public void updateOrderSummary() {
        if (placeOrderDialog == null || !placeOrderDialog.isDisplayable()) {
            return;
        }

        List<CartItem> cartItems = new CartController().getCartItems();
        double subtotal = cartItems.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        
        String province = placeOrderDialog.getSelectedProvince();
        boolean isRush = placeOrderDialog.isRushOrder();
        int rushHours = isRush ? placeOrderDialog.getRushHours() : 0;

        double shippingFee = calculateShippingFee(province, isRush, rushHours);
        double vat = subtotal * 0.1;
        double total = subtotal + shippingFee + vat;
        
        OrderSummary summary = new OrderSummary();
        summary.setSubtotal(subtotal);
        summary.setShippingFee(shippingFee);
        summary.setVat(vat);
        summary.setTotal(total);

        placeOrderDialog.updateDisplay(summary);
    }

    // Called from PlaceOrderDialog, this method creates the pending order and launches VNPay
    public void finalizeOrderAndPay(DeliveryInfo deliveryInfo, boolean isRush, int rushHours) {
        System.out.println("Finalizing order and proceeding to VNPay...");

        // 1. Get cart contents
        List<CartItem> cartItems = new CartController().getCartItems();
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(owner, "Giỏ hàng của bạn trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            // 2. Start Transaction
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 3. Create Order
            OrderController transOrderController = new OrderController(conn);
            
            // Calculate final totals based on final info
            double subtotal = cartItems.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
            double shippingFee = calculateShippingFee(deliveryInfo.getProvince(), isRush, rushHours);
            double vat = subtotal * 0.1;
            double finalTotal = subtotal + shippingFee + vat;
            
            // Create Order object
            Order order = new Order();
            order.setUserId(currentUser.getId());
            order.setTotalAmount(subtotal);
            order.setStatus("pending");
            order.setRushOrder(isRush);
            order.setDeliveryFee(shippingFee);
            order.setVat(vat);
            order.setTotalPayment(finalTotal);
            deliveryInfo.setRushDeliveryHours(rushHours); // Save rush hours

            // Insert complete order in one transaction
            int orderId = transOrderController.placeCompleteOrder(order, cartItems, deliveryInfo);

            if (orderId == -1) {
                throw new SQLException("Failed to create order, no ID obtained.");
            }

            // 4. Commit Transaction
            conn.commit();
            System.out.println("Pending order created successfully with ID: " + orderId);

            // 5. Build VNPay URL
            String vnp_TxnRef = String.valueOf(orderId);
            String vnp_Amount = String.valueOf((int)finalTotal * 100);
            String vnp_OrderInfo = "Thanh toan don hang " + vnp_TxnRef;
            
            System.out.println("Building VNPay URL with parameters:");
            System.out.println("- TxnRef: " + vnp_TxnRef);
            System.out.println("- Amount: " + vnp_Amount);
            System.out.println("- OrderInfo: " + vnp_OrderInfo);
            System.out.println("- Final Total: " + finalTotal);
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VnpayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnpayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", VnpayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", vnp_Amount);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            String queryUrl = VnpayConfig.createQueryUrl(vnp_Params);
            String hashData = VnpayConfig.getHashData(vnp_Params);
            String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData);
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            
            System.out.println("Generated VNPay URL:");
            System.out.println(queryUrl);
            System.out.println("Hash data: " + hashData);
            System.out.println("Secure hash: " + vnp_SecureHash);
            
            // 6. Launch Browser with proper URL encoding
            try {
                System.out.println("Attempting to open browser...");
                Desktop.getDesktop().browse(new URI(queryUrl));
                System.out.println("Browser opened successfully!");
            } catch (Exception e) {
                System.err.println("Failed to open browser: " + e.getMessage());
                System.err.println("URL was: " + queryUrl);
                e.printStackTrace();
                // Show the URL to user so they can copy and paste
                JOptionPane.showMessageDialog(owner, 
                    "Không thể mở trình duyệt tự động.\n" +
                    "Vui lòng copy URL sau và mở trong trình duyệt:\n\n" + queryUrl,
                    "Mở trình duyệt thủ công",
                    JOptionPane.INFORMATION_MESSAGE);
            }

            // 7. Close Dialogs
            if (placeOrderDialog != null) placeOrderDialog.dispose();
            if (cartDialog != null) cartDialog.dispose();

        } catch (Exception e) {
            System.err.println("Transaction failed, rolling back.");
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(owner, "Không thể tạo đơn hàng. Đã xảy ra lỗi.", "Lỗi Giao Dịch", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private double calculateShippingFee(String province, boolean isRush, int rushHours) {
        double fee = 20000; // Base fee for Hanoi/HCMC/DN
        if (!"Hà Nội".equalsIgnoreCase(province) && !"Hồ Chí Minh".equalsIgnoreCase(province) && !"Đà Nẵng".equalsIgnoreCase(province)) {
            fee += 15000; // Extra for other provinces
        }

        if (isRush) {
            // Base rush fee
            fee += 30000; 
            // Extra fee for every hour faster than 24h
            if (rushHours < 24 && rushHours > 0) {
                fee += (24 - rushHours) * 2000; // 2,000 VND per hour saved
            }
        }
        return fee;
    }

    @Override
    public void onPaymentSuccess(String orderIdStr, String amount, String orderInfo) {
        // This is called by the VnpayCallbackServer on the Swing thread
        System.out.println("CheckoutManager received Payment SUCCESS for order: " + orderIdStr);

        Connection conn = null;
        try {
            int orderId = Integer.parseInt(orderIdStr);

            System.out.println("Transaction started. Auto-commit is OFF.");
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Update order status to "approved"
            System.out.println("Updating order status...");
            OrderController transOrderController = new OrderController(conn);
            if (!transOrderController.updateOrderStatus(orderId, "approved")) {
                 throw new SQLException("Failed to update order status to approved.");
            }

            // 2. Update product stock
            System.out.println("Updating product stock...");
            ProductController trans_productController = new ProductController(conn);
            trans_productController.updateStockAfterOrder(orderId);

            // 3. Clear cart
            System.out.println("Clearing cart...");
            CartController trans_cartController = new CartController(conn);
            trans_cartController.clearCart(currentUser.getId());

            // 4. Commit transaction
            conn.commit();
            System.out.println("TRANSACTION COMMITTED SUCCESSFULLY.");

            // 5. All DB operations are done, now update the UI
            SwingUtilities.invokeLater(() -> {
                if (cartDialog != null) {
                    cartDialog.dispose(); // Close the cart dialog
                }
                JOptionPane.showMessageDialog(owner,
                        "Thanh toán cho đơn hàng #" + orderId + " thành công!",
                        "Thanh toán thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                owner.refreshAllData(); // Tell the main frame to refresh itself
            });

        } catch (Exception e) {
            System.err.println("FATAL: An error occurred during the transaction. Rolling back...");
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back.");
                } catch (SQLException ex) {
                    System.err.println("Failed to rollback transaction.");
                    ex.printStackTrace();
                }
            }
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(owner,
                    "Đã xảy ra lỗi nghiêm trọng khi cập nhật đơn hàng.\n" +
                    "Mọi thay đổi đã được hoàn tác. Vui lòng thử lại.", "Lỗi Giao Dịch", JOptionPane.ERROR_MESSAGE));
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Return connection to the pool
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPaymentFailure(String orderId, String errorCode) {
        // Handle payment failure (e.g., show a message)
        SwingUtilities.invokeLater(() -> {
            if (cartDialog != null) {
                cartDialog.dispose();
            }
            JOptionPane.showMessageDialog(owner,
                    "Thanh toán cho đơn hàng #" + orderId + " thất bại. Mã lỗi: " + errorCode,
                    "Thanh toán thất bại",
                    JOptionPane.WARNING_MESSAGE);
        });
    }
}