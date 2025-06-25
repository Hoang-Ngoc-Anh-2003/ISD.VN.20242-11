package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DatabaseConnection;
import model.entity.CD;

public class CDDAO extends ProductDAO {
    // lay thong tin CD
    public CD getCDInfor (int product_id){
        CD cd = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                     "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                     "c.artists, c.record_label, c.tracklist, c.genre, c.release_date " +
                     "FROM products p " +
                     "INNER JOIN cds c ON p.product_id = c.cd_id " +
                     "WHERE p.category = 'CD' AND p.product_id = ? ";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, product_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cd = new CD();
                cd.setProductId(rs.getInt("product_id"));
                cd.setTitle(rs.getString("title"));
                cd.setCategory(rs.getString("category"));
                cd.setValue(rs.getDouble("value"));
                cd.setPrice(rs.getDouble("price"));
                cd.setBarcode(rs.getString("barcode"));
                cd.setDescription(rs.getString("description"));
                cd.setQuantity(rs.getInt("quantity"));
                cd.setWeight(rs.getString("weight")); // Chuyển sang getDouble
                cd.setDimensions(rs.getString("dimensions"));
                cd.setWarehouseEntryDate(rs.getString("warehouse_entry_date")); // Sử dụng getDate
                cd.setArtists(rs.getString("artists"));
                cd.setRecordLabel(rs.getString("record_label"));
                cd.setTracklist(rs.getString("tracklist"));
                cd.setGenre(rs.getString("genre"));
                cd.setReleaseDate(rs.getString("release_date")); // Sử dụng getDate
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return cd;
    }
    
    // lay danh sach san pham de hien thi tren bang "Panel"
    public List<CD> getAllCDs() {
        List<CD> cds = new ArrayList<>();
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price, p.barcode, " +
                     "p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date, " +
                     "c.artists, c.record_label, c.tracklist, c.genre, c.release_date " +
                     "FROM products p " +
                     "INNER JOIN cds c ON p.product_id = c.cd_id " +
                     "WHERE p.category = 'CD'";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                CD cd = new CD(
                    rs.getInt("product_id"),
                    rs.getString("title"),
                    rs.getString("category"), 
                    rs.getDouble("value"),
                    rs.getDouble("price"),
                    rs.getString("barcode"),
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getString("weight"),
                    rs.getString("dimensions"),
                    rs.getString("warehouse_entry_date"),
                    rs.getString("artists"),
                    rs.getString("record_label"),
                    rs.getString("tracklist"),
                    rs.getString("genre"),
                    rs.getString("release_date")
                );
                cds.add(cd);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return cds;
    }
    
    private String generateUniqueBarcode(Connection conn) throws SQLException {
        String barcode;
        boolean isUnique = false;
        do {
            barcode = String.format("%013d", (long)(Math.random() * 1_000_000_000_0000L));
            String checkSql = "SELECT COUNT(*) FROM Products WHERE barcode = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setString(1, barcode);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    isUnique = true;
                }
            }
        } while (!isUnique);
        return barcode;
    }
    
    public boolean addCD(CD cd) {
        String productSQL = "INSERT INTO Products (title, category, value, price, barcode, description, quantity, weight, dimensions, warehouse_entry_date) " +
                            "VALUES (?, 'CD', ?, ?, ?, ?, ?, ?, ?, ?) RETURNING product_id";
        String cdSQL = "INSERT INTO CDs (cd_id, artists, record_label, tracklist, genre, release_date) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(productSQL);
             PreparedStatement cdStmt = conn.prepareStatement(cdSQL)) {
    
            String barcode = generateUniqueBarcode(conn);
            cd.setBarcode(barcode);
    
            productStmt.setString(1, cd.getTitle());
            productStmt.setDouble(2, cd.getValue());
            productStmt.setDouble(3, cd.getPrice());
            productStmt.setString(4, barcode);
            productStmt.setString(5, cd.getDescription());
            productStmt.setInt(6, cd.getQuantity());
            productStmt.setString(7, cd.getWeight());
            productStmt.setString(8, cd.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(cd.getWarehouseEntryDate()));
    
            ResultSet rs = productStmt.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt(1);
                cd.setProductId(productId);
    
                cdStmt.setInt(1, productId);
                cdStmt.setString(2, cd.getArtists());
                cdStmt.setString(3, cd.getRecordLabel());
                cdStmt.setString(4, cd.getTracklist());
                cdStmt.setString(5, cd.getGenre());
                if (cd.getReleaseDate() != null && !cd.getReleaseDate().isEmpty()) {
                    cdStmt.setDate(6, java.sql.Date.valueOf(cd.getReleaseDate()));
                } else {
                    cdStmt.setNull(6, java.sql.Types.DATE);
                }
    
                return cdStmt.executeUpdate() > 0;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật CD
    public boolean updateCD(CD cd) {
        String updateProductSql = "UPDATE Products SET title = ?, value = ?, price = ?, barcode = ?, " +
                                  "description = ?, quantity = ?, weight = ?, dimensions = ?, warehouse_entry_date = ? " +
                                  "WHERE product_id = ?";

        String updateCDSql = "UPDATE CDs SET artists = ?, record_label = ?, tracklist = ?, " +
                             "genre = ?, release_date = ? WHERE cd_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(updateProductSql);
             PreparedStatement cdStmt = conn.prepareStatement(updateCDSql)) {

            // Cập nhật Products
            productStmt.setString(1, cd.getTitle());
            productStmt.setDouble(2, cd.getValue());
            productStmt.setDouble(3, cd.getPrice());
            productStmt.setString(4, cd.getBarcode());
            productStmt.setString(5, cd.getDescription());
            productStmt.setInt(6, cd.getQuantity());
            productStmt.setString(7, cd.getWeight());
            productStmt.setString(8, cd.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(cd.getWarehouseEntryDate()));
            productStmt.setInt(10, cd.getProductId());
            productStmt.executeUpdate();

            // Cập nhật CDs
            cdStmt.setString(1, cd.getArtists());
            cdStmt.setString(2, cd.getRecordLabel());
            cdStmt.setString(3, cd.getTracklist());
            cdStmt.setString(4, cd.getGenre());

            if (cd.getReleaseDate() != null && !cd.getReleaseDate().isEmpty()) {
                cdStmt.setDate(5, java.sql.Date.valueOf(cd.getReleaseDate()));
            } else {
                cdStmt.setNull(5, java.sql.Types.DATE);
            }
            
            cdStmt.setInt(6, cd.getProductId());
            cdStmt.executeUpdate();
            
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCD(int productId) {
        String deleteCDSql = "DELETE FROM Cds WHERE cd_id = ?";
        String deleteProductSql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement cdStmt = conn.prepareStatement(deleteCDSql);
             PreparedStatement productStmt = conn.prepareStatement(deleteProductSql)) {

            // Xóa từ bảng Cds trước
            cdStmt.setInt(1, productId);
            cdStmt.executeUpdate();

            // Xóa từ bảng Products sau
            productStmt.setInt(1, productId);
            productStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CD getCDById(int productId) {
        String sql = "SELECT * FROM Products JOIN CDs ON Products.product_id = CDs.product_id WHERE Products.product_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
    
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCD(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // so luong CD
    public static int getCDCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM CDs";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    

    private CD mapResultSetToCD(ResultSet rs) throws SQLException {
        return new CD(
            rs.getInt("product_id"),
            rs.getString("title"),
            rs.getString("category"),
            rs.getDouble("value"),
            rs.getDouble("price"),
            rs.getString("barcode"),
            rs.getString("description"),
            rs.getInt("quantity"),
            rs.getString("weight"),
            rs.getString("dimensions"),
            rs.getString("warehouse_entry_date"),
            rs.getString("artists"),
            rs.getString("record_label"),
            rs.getString("tracklist"),
            rs.getString("genre"),
            rs.getString("release_date")
        );
    }

    private static CDDAO instance;

    public static CDDAO getInstance() {
        if (instance == null) {
            instance = new CDDAO();
        }
        return instance;
    }

    // Dùng cho test
    public static void setMockInstance(CDDAO mock) {
        instance = mock;
    }
}
