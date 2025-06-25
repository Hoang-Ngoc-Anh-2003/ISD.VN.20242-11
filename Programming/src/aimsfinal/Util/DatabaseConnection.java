package aimsfinal.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // PostgreSQL configuration for user's database
    private static final String URL = "jdbc:postgresql://localhost:5432/aimsdb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin"; // User's PostgreSQL password
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to PostgreSQL database 'isd' successfully!");
            } catch (ClassNotFoundException e) {
                System.err.println("PostgreSQL JDBC Driver not found. Please add postgresql-42.7.2.jar to classpath.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Failed to connect to PostgreSQL database 'isd'.");
                System.err.println("Please make sure:");
                System.err.println("1. PostgreSQL is running on localhost:5432");
                System.err.println("2. Database 'isd' exists");
                System.err.println("3. Username: postgres, Password: kai2604");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} 