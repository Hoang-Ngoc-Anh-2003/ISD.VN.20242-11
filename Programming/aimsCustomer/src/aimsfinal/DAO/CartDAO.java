package aimsfinal.DAO;

import aimsfinal.Model.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private Connection connection;
    
    public CartDAO() {
        this.connection = null;
    }

    public CartDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            return this.connection;
        }
        return DatabaseConnection.getConnection();
    }
    
    // Thêm sản phẩm vào giỏ hàng
    public boolean addToCart(int userId, int productId, int quantity) {
        try (Connection conn = getConnection()) {
            // 1. Tìm cart_id của user, nếu chưa có thì tạo mới
            int cartId = -1;
            String findCartSql = "SELECT cart_id FROM carts WHERE user_id = ?";
            try (PreparedStatement findCartStmt = conn.prepareStatement(findCartSql)) {
                findCartStmt.setInt(1, userId);
                ResultSet rs = findCartStmt.executeQuery();
                if (rs.next()) {
                    cartId = rs.getInt("cart_id");
                } else {
                    // Tạo mới cart
                    String createCartSql = "INSERT INTO carts (user_id) VALUES (?) RETURNING cart_id";
                    try (PreparedStatement createCartStmt = conn.prepareStatement(createCartSql)) {
                        createCartStmt.setInt(1, userId);
                        ResultSet crs = createCartStmt.executeQuery();
                        if (crs.next()) {
                            cartId = crs.getInt("cart_id");
                        }
                    }
                }
            }
            if (cartId == -1) return false;

            // 2. Kiểm tra sản phẩm đã có trong cart chưa
            String checkSql = "SELECT quantity FROM cartitems WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, cartId);
                checkStmt.setInt(2, productId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // Đã có, cập nhật số lượng
                    int oldQty = rs.getInt("quantity");
                    String updateSql = "UPDATE cartitems SET quantity = ? WHERE cart_id = ? AND product_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, oldQty + quantity);
                        updateStmt.setInt(2, cartId);
                        updateStmt.setInt(3, productId);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // Chưa có, thêm mới
                    String insertSql = "INSERT INTO cartitems (cart_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, cartId);
                        insertStmt.setInt(2, productId);
                        insertStmt.setInt(3, quantity);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy danh sách sản phẩm trong giỏ hàng của user
    public List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT ci.product_id, p.title, p.price, ci.quantity " +
                         "FROM carts c " +
                         "JOIN cartitems ci ON c.cart_id = ci.cart_id " +
                         "JOIN products p ON ci.product_id = p.product_id " +
                         "WHERE c.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("title"));
                    item.setPrice(rs.getDouble("price"));
                    item.setQuantity(rs.getInt("quantity"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    public boolean updateItemQuantity(int itemId, int quantity) {
        // TODO: Implement update item quantity
        return true;
    }
    
    public boolean removeItem(int cartId, int productId) {
        String sql = "DELETE FROM cartitems WHERE cart_id = ? AND product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void clearCart(int userId) throws SQLException {
        Connection conn = getConnection();
        String sql = "DELETE FROM cartitems WHERE cart_id = (SELECT cart_id FROM carts WHERE user_id = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } finally {
            if (this.connection == null && conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }

    public static int getCartIdByUserId(int userId) {
        String sql = "SELECT cart_id FROM carts WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cart_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}