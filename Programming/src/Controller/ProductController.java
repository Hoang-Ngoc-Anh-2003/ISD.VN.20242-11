package Controller;

import Model.DatabaseConnector;
import java.sql.*;

/**
 * Controller quản lý các thao tác liên quan đến sản phẩm
 */
public abstract class ProductController {

    /**
     * Lấy thông tin loại sản phẩm (book, cd, dvd, lp) theo ID
     *
     * @param productId ID của sản phẩm cần truy vấn
     * @return Loại sản phẩm (category)
     */
    public static String getProductCategory(int productId) {
        String category = null;
        String sql = "SELECT category FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                category = rs.getString("category");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy loại sản phẩm: " + e.getMessage());
        }

        return category;
    }
    
    /**
     * Đếm số lượng sản phẩm theo loại
     *
     * @param tableName Tên bảng cần đếm số lượng
     * @return Số lượng sản phẩm
     */
    public int countProducts(String tableName) {
        int count = 0;

        // Validate tableName to prevent SQL injection
        if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$") || tableName.equalsIgnoreCase("Products")) {
            throw new IllegalArgumentException("Invalid or unsafe table name: " + tableName);
        }
        String query = "SELECT COUNT(*) FROM " + tableName;
        // Suppress IDE warning: tableName is validated above
        // noinspection SqlSourceToSinkFlow
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm số lượng sản phẩm: " + e.getMessage());
        }

        return count;
    }
}
