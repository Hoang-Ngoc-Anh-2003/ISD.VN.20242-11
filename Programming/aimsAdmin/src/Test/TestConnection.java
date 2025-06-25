package Test;

import java.sql.Connection;
import java.sql.SQLException;

import Utils.DatabaseConnection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database OK!");
            } else {
                System.out.println("Database ERROR!");
            }
        } catch (SQLException e) {
            System.err.println("Loi khi kiem tra ket noi!");
            e.printStackTrace();
        }
    }
}
