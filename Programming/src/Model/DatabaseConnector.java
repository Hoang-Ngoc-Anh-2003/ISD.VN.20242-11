package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp quản lý kết nối với cơ sở dữ liệu
 */
public class DatabaseConnector {
    // Thông tin kết nối cơ sở dữ liệu
    private static final String URL = "jdbc:postgresql://localhost:5432/aims";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    /**
     * Tạo và trả về kết nối đến cơ sở dữ liệu PostgreSQL
     *
     * @return Connection - Đối tượng kết nối đến cơ sở dữ liệu
     * @throws SQLException Khi không thể kết nối được đến cơ sở dữ liệu
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy PostgreSQL JDBC Driver.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Đóng kết nối an toàn
     *
     * @param connection Kết nối cần đóng
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
}
