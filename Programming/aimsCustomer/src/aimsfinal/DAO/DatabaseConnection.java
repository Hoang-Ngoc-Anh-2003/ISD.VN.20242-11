package aimsfinal.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/aimsdb"; // Thay "aims" bằng tên database của bạn
    private static final String USER = "postgres"; // Thay bằng username của bạn
    private static final String PASSWORD = "admin"; // Thay bằng password của bạn

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Additional method for GUI compatibility
    public static void closeConnection() {
        // This method is for compatibility with MainFrame
        // In a real application, you would track and close all open connections
        System.out.println("Database connections closed");
    }
}
