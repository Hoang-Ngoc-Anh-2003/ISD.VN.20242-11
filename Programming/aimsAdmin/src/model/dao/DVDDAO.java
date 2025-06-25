package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DatabaseConnection;
import model.entity.DVD;

public class DVDDAO extends ProductDAO {
    // lay thong tin DVD
    public DVD getDVDInfor (int product_id){
        DVD dvd = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                     "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                     "d.disc_type, d.director, d.runtime, d.studio, d.language, d.subtitles, d.release_date, d.genre " +
                     "FROM products p " +
                     "INNER JOIN dvds d ON p.product_id = d.dvd_id " +
                     "WHERE p.category = 'DVD' AND p.product_id = ? ";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, product_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dvd = new DVD();
                dvd.setProductId(rs.getInt("product_id"));
                dvd.setTitle(rs.getString("title"));
                dvd.setCategory(rs.getString("category"));
                dvd.setValue(rs.getDouble("value"));
                dvd.setPrice(rs.getDouble("price"));
                dvd.setBarcode(rs.getString("barcode"));
                dvd.setDescription(rs.getString("description"));
                dvd.setQuantity(rs.getInt("quantity"));
                dvd.setWeight(rs.getString("weight")); // Chuyển sang getDouble
                dvd.setDimensions(rs.getString("dimensions"));
                dvd.setWarehouseEntryDate(rs.getString("warehouse_entry_date")); // Sử dụng getDate
                dvd.setDiscType(rs.getString("disc_type"));
                dvd.setDirector(rs.getString("director"));
                dvd.setRuntime(rs.getInt("runtime"));
                dvd.setStudio(rs.getString("studio"));
                dvd.setLanguage(rs.getString("language"));
                dvd.setSubtitles(rs.getString("subtitles"));
                dvd.setReleaseDate(rs.getString("release_date")); // Sử dụng getDate
                dvd.setGenre(rs.getString("genre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return dvd;
    }
    
    // lay danh sach san pham de hien thi tren bang "Panel"
    public List<DVD> getAllDVDs() {
        List<DVD> dvds = new ArrayList<>();
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price, p.barcode, " +
                     "p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date, " +
                     "d.disc_type, d.director, d.runtime, d.studio, d.language, " +
                     "d.subtitles, d.release_date, d.genre " +
                     "FROM Products p " +
                     "INNER JOIN DVDs d ON p.product_id = d.dvd_id " +
                     "WHERE p.category = 'DVD'";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                DVD dvd = new DVD(
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
                rs.getString("disc_type"),
                rs.getString("director"),
                rs.getInt("runtime"),
                rs.getString("studio"),
                rs.getString("language"),
                rs.getString("subtitles"),
                rs.getString("release_date"),
                rs.getString("genre")
                );
                dvds.add(dvd);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return dvds;
    }

    // Cập nhật DVD
    public boolean updateDVD(DVD dvd) {
        String updateProductSql = "UPDATE Products SET title = ?, value = ?, price = ?, barcode = ?, " +
                                  "description = ?, quantity = ?, weight = ?, dimensions = ?, warehouse_entry_date = ? " +
                                  "WHERE product_id = ?";

        String updateDVDSql = "UPDATE DVDs SET disc_type = ?, director = ?, runtime = ?, " +
                              "studio = ?, language = ?, subtitles = ?, release_date = ?, genre = ? WHERE dvd_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(updateProductSql);
             PreparedStatement dvdStmt = conn.prepareStatement(updateDVDSql)) {

            // Cập nhật Products
            productStmt.setString(1, dvd.getTitle());
            productStmt.setDouble(2, dvd.getValue());
            productStmt.setDouble(3, dvd.getPrice());
            productStmt.setString(4, dvd.getBarcode());
            productStmt.setString(5, dvd.getDescription());
            productStmt.setInt(6, dvd.getQuantity());
            productStmt.setString(7, dvd.getWeight());
            productStmt.setString(8, dvd.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(dvd.getWarehouseEntryDate()));
            productStmt.setInt(10, dvd.getProductId());
            productStmt.executeUpdate();

            // Cập nhật DVDs
            dvdStmt.setString(1, dvd.getDiscType());
            dvdStmt.setString(2, dvd.getDirector());
            dvdStmt.setInt(3, dvd.getRuntime());
            dvdStmt.setString(4, dvd.getStudio());
            dvdStmt.setString(5, dvd.getLanguage());
            dvdStmt.setString(6, dvd.getSubtitles());
            
            if (dvd.getReleaseDate() != null && !dvd.getReleaseDate().isEmpty()) {
                dvdStmt.setDate(7, java.sql.Date.valueOf(dvd.getReleaseDate()));
            } else {
                dvdStmt.setNull(7, java.sql.Types.DATE);
            }
            
            dvdStmt.setString(8, dvd.getGenre());
            dvdStmt.setInt(9, dvd.getProductId());
            dvdStmt.executeUpdate();

            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDVD(int productId) {
        String deleteDVDsSql = "DELETE FROM Dvds WHERE dvd_id = ?";
        String deleteProductSql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement dvdsStmt = conn.prepareStatement(deleteDVDsSql);
             PreparedStatement productStmt = conn.prepareStatement(deleteProductSql)) {

            // Xóa từ bảng Dvds trước
            dvdsStmt.setInt(1, productId);
            dvdsStmt.executeUpdate();

            // Xóa từ bảng Products sau
            productStmt.setInt(1, productId);
            productStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
    

    public DVD getDVDById(int productId) {
        String sql = "SELECT * FROM Products JOIN DVDs ON Products.product_id = DVDs.product_id WHERE Products.product_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDVD(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // so luong DVD
    public static int getDVDCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM DVDs";
    
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
    
    public boolean addDVD(DVD dvd) {
        String productSQL = "INSERT INTO Products (title, category, value, price, barcode, description, quantity, weight, dimensions, warehouse_entry_date) " +
                            "VALUES (?, 'DVD', ?, ?, ?, ?, ?, ?, ?, ?) RETURNING product_id";
        String dvdSQL = "INSERT INTO DVDs (dvd_id, disc_type, director, runtime, studio, language, subtitles, release_date, genre) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(productSQL);
             PreparedStatement dvdStmt = conn.prepareStatement(dvdSQL)) {
    
            String barcode = generateUniqueBarcode(conn);
            dvd.setBarcode(barcode);
    
            productStmt.setString(1, dvd.getTitle());
            productStmt.setDouble(2, dvd.getValue());
            productStmt.setDouble(3, dvd.getPrice());
            productStmt.setString(4, barcode);
            productStmt.setString(5, dvd.getDescription());
            productStmt.setInt(6, dvd.getQuantity());
            productStmt.setString(7, dvd.getWeight());
            productStmt.setString(8, dvd.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(dvd.getWarehouseEntryDate()));
    
            ResultSet rs = productStmt.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt(1);
                dvd.setProductId(productId);
    
                dvdStmt.setInt(1, productId);
                dvdStmt.setString(2, dvd.getDiscType());
                dvdStmt.setString(3, dvd.getDirector());
                dvdStmt.setInt(4, dvd.getRuntime());
                dvdStmt.setString(5, dvd.getStudio());
                dvdStmt.setString(6, dvd.getLanguage());
                dvdStmt.setString(7, dvd.getSubtitles());
                if (dvd.getReleaseDate() != null && !dvd.getReleaseDate().isEmpty()) {
                    dvdStmt.setDate(8, java.sql.Date.valueOf(dvd.getReleaseDate()));
                } else {
                    dvdStmt.setNull(8, java.sql.Types.DATE);
                }
                dvdStmt.setString(9, dvd.getGenre());
    
                return dvdStmt.executeUpdate() > 0;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    private DVD mapResultSetToDVD(ResultSet rs) throws SQLException {
        return new DVD(
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
            rs.getString("disc_type"),
            rs.getString("director"),
            rs.getInt("runtime"),
            rs.getString("studio"),
            rs.getString("language"),
            rs.getString("subtitles"),
            rs.getString("release_date"),
            rs.getString("genre")
        );
    }

    private static DVDDAO instance;

    public static DVDDAO getInstance() {
        if (instance == null) {
            instance = new DVDDAO();
        }
        return instance;
    }
    
    // Dùng cho test
    public static void setMockInstance(DVDDAO mock) {
        instance = mock;
    }
}
