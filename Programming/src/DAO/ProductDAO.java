package aimsfinal.DAO;

import aimsfinal.Model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        this.connection = null;
    }

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            return this.connection;
        }
        return DatabaseConnection.getConnection();
    }

    // lay thong tin loai san pham (book, cd, dvd, lp)
    public String getProductCategory(int productId) {
        String category = null;
        String sql = "SELECT product_id, title, category, value, price, barcode, description, quantity, weight, dimensions, warehouse_entry_date " +
                     "FROM Products " +
                     "WHERE product_id = ?";

        try (Connection conn = getConnection();
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

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Product createProductFromResultSet(ResultSet rs) throws SQLException {
        // Trả về Product cơ bản với các trường chính
        return new aimsfinal.Model.Product() {{
            setProductId(rs.getInt("product_id"));
            setTitle(rs.getString("title"));
            setCategory(rs.getString("category"));
            setValue(rs.getDouble("value"));
            setPrice(rs.getDouble("price"));
            setBarcode(rs.getString("barcode"));
            setDescription(rs.getString("description"));
            setQuantity(rs.getInt("quantity"));
            setWeight(rs.getString("weight"));
            setDimensions(rs.getString("dimensions"));
            setWarehouseEntryDate(rs.getString("warehouse_entry_date"));
        }};
    }

    // Additional methods for Controller compatibility
    public List<Product> getAll() {
        return getAllProducts();
    }
    
    public List<Product> search(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE title ILIKE ? OR description ILIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(createProductFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public Product getById(int id) {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createProductFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Additional methods for Controller compatibility
    public int insertProduct(Product product) {
        String sql = "INSERT INTO Products (title, category, value, price, barcode, description, quantity, weight, dimensions, warehouse_entry_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getTitle());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getValue());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getBarcode());
            stmt.setString(6, product.getDescription());
            stmt.setInt(7, product.getQuantity());
            stmt.setString(8, product.getWeight());
            stmt.setString(9, product.getDimensions());
            stmt.setString(10, product.getWarehouseEntryDate());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Products SET title = ?, category = ?, value = ?, price = ?, barcode = ?, description = ?, quantity = ?, weight = ?, dimensions = ?, warehouse_entry_date = ? WHERE product_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getTitle());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getValue());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getBarcode());
            stmt.setString(6, product.getDescription());
            stmt.setInt(7, product.getQuantity());
            stmt.setString(8, product.getWeight());
            stmt.setString(9, product.getDimensions());
            stmt.setString(10, product.getWarehouseEntryDate());
            stmt.setInt(11, product.getProductId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStock(int productId, int quantityToDecrease) throws SQLException {
        Connection conn = getConnection();
        String sql = "UPDATE Products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityToDecrease);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantityToDecrease);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Could not update stock for product ID " + productId + ". Product may be out of stock or not exist.");
            }
            return true;
        } finally {
            if (this.connection == null && conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
}
