package aimsfinal.DAO;

import aimsfinal.Model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    private Connection connection;
    
    public PaymentDAO() {
        this.connection = null;
    }
    
    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            return this.connection;
        }
        return DatabaseConnection.getConnection();
    }
    
    public boolean create(Payment payment) throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO payments (order_id, amount, method, status, payment_date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getOrderId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getPaymentStatus() != null ? payment.getPaymentStatus() : "pending");
            return stmt.executeUpdate() > 0;
        } finally {
            if (this.connection == null && conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
    
    public Payment getById(int id) throws SQLException {
        // TODO: Implement get payment by id
        return null;
    }
    
    public List<Payment> getAll() throws SQLException {
        // TODO: Implement get all payments
        return new ArrayList<>();
    }
    
    public boolean update(Payment payment) throws SQLException {
        // TODO: Implement update payment
        return true;
    }
    
    public boolean delete(int id) throws SQLException {
        // TODO: Implement delete payment
        return true;
    }
    
    public Payment getByOrderId(int orderId) throws SQLException {
        // TODO: Implement get payment by order id
        return null;
    }
    
    public boolean updatePaymentStatusByOrderId(int orderId, String status) throws SQLException {
        String sql = "UPDATE payments SET status = ?, payment_date = CURRENT_TIMESTAMP WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, orderId);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }
} 