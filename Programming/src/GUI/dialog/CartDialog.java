package aimsfinal.GUI.dialog;

import aimsfinal.Controller.CartController;
import aimsfinal.Controller.CheckoutManager;
import aimsfinal.Model.CartItem;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CartDialog extends JDialog {
    private CartController cartController;
    private CheckoutManager checkoutManager;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public CartDialog(Frame parent, CheckoutManager checkoutManager) {
        super(parent, "Giỏ hàng", true);
        this.cartController = new CartController();
        this.checkoutManager = checkoutManager;
        
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initializeComponents();
        setupLayout();
        loadCartItems();
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Tên sản phẩm", "Giá", "Số lượng", "Tổng"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(tableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        totalLabel = new JLabel("Tổng cộng: 0 VND");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Bảng giỏ hàng
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel dưới
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tổng cộng
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        
        // Nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkoutButton = new JButton("Thanh toán");
        JButton removeButton = new JButton("Xóa");
        JButton closeButton = new JButton("Đóng");
        
        checkoutButton.addActionListener(e -> checkout());
        removeButton.addActionListener(e -> removeSelectedItem());
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(checkoutButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(closeButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        tableModel.setRowCount(0);
        List<CartItem> items = cartController.getCartItems();
        double total = 0;
        
        if (items == null || items.isEmpty()) {
            // Disable checkout button if cart is empty
            // This is handled in the setupLayout for simplicity, but a check here is good
            return;
        }

        for (CartItem item : items) {
            double itemTotal = item.getPrice() * item.getQuantity();
            total += itemTotal;
            
            Object[] row = {
                item.getProductId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity(),
                itemTotal
            };
            tableModel.addRow(row);
        }
        
        totalLabel.setText(String.format("Tổng cộng: %.2f VND", total));
    }

    public void refreshCartData() {
        loadCartItems();
    }

    private void checkout() {
        // Check if cart is empty before proceeding
        if (cartController.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống! Không thể thanh toán.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // The dialog now asks for delivery type and tells the manager to proceed.
        int rushChoice = JOptionPane.showConfirmDialog(this,
            "Bạn có muốn giao hàng nhanh không?\n" +
            "• Giao hàng thường: 3-5 ngày\n" +
            "• Giao hàng nhanh: 1-2 ngày (phí thêm 20% vào giá trị đơn hàng)",
            "Chọn loại giao hàng",
            JOptionPane.YES_NO_OPTION);

        // We proceed regardless of the choice (YES, NO). If the user closes the dialog, we do nothing.
        if (rushChoice != JOptionPane.CLOSED_OPTION) {
            boolean isRush = (rushChoice == JOptionPane.YES_OPTION);
            
            // Hand over control to the checkout manager
            checkoutManager.proceedToPayment(isRush);

            // Close the cart dialog as the flow is now with the manager
            dispose();
        }
    }

    private void removeSelectedItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Lấy cart_id hiện tại của user
        int userId = aimsfinal.GUI.Session.getCurrentUser().getId();
        int cartId = aimsfinal.DAO.CartDAO.getCartIdByUserId(userId);
        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = cartController.removeItem(cartId, productId);
            if (success) {
                refreshCartData();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}