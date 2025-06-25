package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DatabaseConnection;
import model.entity.Product;

public class ProductDAO {

    // lay thong tin loai san pham (book, cd, dvd, lp)
    public String getProductCategory(int productId) {
        String category = null;
        String sql = "SELECT product_id, title, category, value, price, barcode, description, quantity, weight, dimensions, warehouse_entry_date " +
                     "FROM Products " +
                     "WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                category = rs.getString("category");
                // product = new Product();
                // product.setProductId(rs.getInt("product_id"));
                // product.setTitle(rs.getString("title"));
                // product.setCategory(rs.getString("category"));
                // product.setValue(rs.getDouble("value"));
                // product.setPrice(rs.getDouble("price"));
                // product.setBarcode(rs.getString("barcode"));
                // product.setDescription(rs.getString("description"));
                // product.setQuantity(rs.getInt("quantity"));
                // product.setWeight(rs.getString("weight"));
                // product.setDimensions(rs.getString("dimensions"));
                // product.setWarehouseEntryDate(rs.getString("warehouse_entry_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return category;
    }

    // lay danh sach san pham de hien thi tren bang "Panel"
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(createProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // so luong tung loai san pham
    public static int getProductCount(String tableName) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM " + tableName;
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
                
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return count;
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Product createProductFromResultSet(ResultSet rs) throws SQLException {
        return null; // Override ở subclass
    }

    private static ProductDAO instance;

    public static ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }
}
