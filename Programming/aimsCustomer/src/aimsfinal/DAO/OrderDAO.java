package aimsfinal.DAO;

import aimsfinal.Model.CartItem;
import aimsfinal.Model.DeliveryInfo;
import aimsfinal.Model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection connection;
    
    public OrderDAO() {
        this.connection = null;
    }
    
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            return this.connection;
        }
        return DatabaseConnection.getConnection();
    }
    
    public boolean insertOrder(Order order) throws SQLException {
        // TODO: Implement insert order
        return true;
    }
    
    public Order getOrderById(int id) throws SQLException {
        // TODO: Implement get order by id
        return null;
    }
    
    public List<Order> getAllOrders() throws SQLException {
        // TODO: Implement get all orders
        return new ArrayList<>();
    }
    
    public boolean deleteOrder(int id) throws SQLException {
        Connection conn = getConnection();
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } finally {
            if (this.connection == null && conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
    
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        // TODO: Implement get orders by user id
        return new ArrayList<>();
    }
    
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        Connection conn = getConnection();
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, status);
            stm.setInt(2, orderId);
            return stm.executeUpdate() > 0;
        } finally {
            if (this.connection == null && conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
    
    public boolean createOrder(int userId, List<CartItem> cartItems, String receiver, String phone, String email, String address, String province, String shippingMethod, double shippingFee, boolean isRush) {
        Connection conn = null;
        PreparedStatement orderStmt = null, itemStmt = null, deliveryStmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();

            // 1. Tính tổng tiền
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }
            // Nếu giao hàng nhanh, cộng thêm 20%
            if (isRush) total *= 1.2;

            double vat = total * 0.1;
            double totalPayment = total + vat + shippingFee;

            // 2. Tạo đơn hàng mới
            String orderSql = "INSERT INTO orders (user_id, total_amount, status, order_date, is_rush_order, delivery_fee, vat, total_payment) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING order_id";
            orderStmt = conn.prepareStatement(orderSql);
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, total);
            orderStmt.setString(3, "pending");
            orderStmt.setBoolean(4, isRush);
            orderStmt.setDouble(5, shippingFee);
            orderStmt.setDouble(6, vat);
            orderStmt.setDouble(7, totalPayment);
            rs = orderStmt.executeQuery();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            } else {
                return false;
            }

            // 3. Thêm từng sản phẩm vào orderdetails
            String itemSql = "INSERT INTO orderdetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql);
            for (CartItem item : cartItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            // 4. Lưu thông tin giao hàng (thêm email)
            String deliverySql = "INSERT INTO delivery_info (order_id, receiver_name, phone_number, address, province, shipping_method, shipping_fee, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            deliveryStmt = conn.prepareStatement(deliverySql);
            deliveryStmt.setInt(1, orderId);
            deliveryStmt.setString(2, receiver);
            deliveryStmt.setString(3, phone);
            deliveryStmt.setString(4, address);
            deliveryStmt.setString(5, province);
            deliveryStmt.setString(6, shippingMethod);
            deliveryStmt.setDouble(7, shippingFee);
            deliveryStmt.setString(8, email);
            deliveryStmt.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (orderStmt != null) orderStmt.close(); } catch (Exception e) {}
            try { if (itemStmt != null) itemStmt.close(); } catch (Exception e) {}
            try { if (deliveryStmt != null) deliveryStmt.close(); } catch (Exception e) {}
        }
    }
    
    public int getLastOrderIdForUser(int userId) {
        int orderId = -1;
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "SELECT order_id FROM orders WHERE user_id = ? ORDER BY order_id DESC LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        orderId = rs.getInt("order_id");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.connection == null && conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderId;
    }

    public int createCompleteOrder(Order order, List<CartItem> cartItems, DeliveryInfo deliveryInfo) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement orderStmt = null, itemStmt = null, deliveryStmt = null, paymentStmt = null;
        ResultSet rs = null;
        int orderId = -1;

        try {
            // 1. Insert order and get generated ID
            String orderSql = "INSERT INTO orders (user_id, total_amount, status, is_rush_order, delivery_fee, vat, total_payment) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING order_id";
            orderStmt = conn.prepareStatement(orderSql);
            orderStmt.setInt(1, order.getUserId());
            orderStmt.setDouble(2, order.getTotalAmount());
            orderStmt.setString(3, order.getStatus());
            orderStmt.setBoolean(4, order.isRushOrder());
            orderStmt.setDouble(5, order.getDeliveryFee());
            orderStmt.setDouble(6, order.getVat());
            orderStmt.setDouble(7, order.getTotalPayment());
            rs = orderStmt.executeQuery();
            
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            // 2. Insert order items
            String itemSql = "INSERT INTO orderdetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql);
            for (CartItem item : cartItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            // 3. Insert delivery info
            String deliverySql = "INSERT INTO delivery_info (order_id, receiver_name, phone_number, address, province, shipping_method, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            deliveryStmt = conn.prepareStatement(deliverySql);
            deliveryStmt.setInt(1, orderId);
            deliveryStmt.setString(2, deliveryInfo.getReceiverName());
            deliveryStmt.setString(3, deliveryInfo.getPhone());
            deliveryStmt.setString(4, deliveryInfo.getAddress());
            deliveryStmt.setString(5, deliveryInfo.getProvince());
            deliveryStmt.setString(6, deliveryInfo.getShippingMethod());
            deliveryStmt.setString(7, deliveryInfo.getEmail());
            deliveryStmt.executeUpdate();

            // 4. Insert initial payment record
            String paymentSql = "INSERT INTO payments (order_id, amount, method, status) VALUES (?, ?, ?, 'pending')";
            paymentStmt = conn.prepareStatement(paymentSql);
            paymentStmt.setInt(1, orderId);
            paymentStmt.setDouble(2, order.getTotalPayment());
            paymentStmt.setString(3, "VNPay");
            paymentStmt.executeUpdate();

            return orderId;

        } finally {
            // DO NOT close the connection here, the CheckoutManager will do it.
            // DO NOT commit or rollback here.
            if (rs != null) rs.close();
            if (orderStmt != null) orderStmt.close();
            if (itemStmt != null) itemStmt.close();
            if (deliveryStmt != null) deliveryStmt.close();
            if (paymentStmt != null) paymentStmt.close();
        }
    }

    public boolean updatePaymentStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, status);
            stm.setInt(2, orderId);
            return stm.executeUpdate() > 0;
        }
    }
} 