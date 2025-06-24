package aimsfinal.DAO;

import aimsfinal.Model.DeliveryInfo;
import java.sql.*;

public class DeliveryInfoDAO {
    private Connection connection;

    public DeliveryInfoDAO() {
        this.connection = null;
    }

    public DeliveryInfoDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            return this.connection;
        }
        return DatabaseConnection.getConnection();
    }

    public boolean insertDeliveryInfo(DeliveryInfo info) throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO delivery_info (order_id, receiver_name, phone_number, address, province, shipping_method, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, info.getOrderId());
            stmt.setString(2, info.getReceiverName());
            stmt.setString(3, info.getPhone());
            stmt.setString(4, info.getAddress());
            stmt.setString(5, info.getProvince());
            stmt.setString(6, info.getShippingMethod());
            stmt.setString(7, info.getEmail());
            stmt.executeUpdate();
        } finally {
            if (this.connection == null && conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        return true;
    }

    public boolean deleteByOrderId(int orderId) throws SQLException {
        // TODO: Implement delete by order id
        return true;
    }
}