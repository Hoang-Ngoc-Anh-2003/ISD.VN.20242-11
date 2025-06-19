package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }

    public static <T> List<T> executeQueryList(String sql, ResultSetHandler<T> handler) {
        List<T> result = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(handler.handle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn: " + e.getMessage());
        }
        return result;
    }
}

