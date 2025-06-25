package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DatabaseConnection;
import model.entity.LP;

public class LPDAO extends ProductDAO {
    // lay thong tin LP
    public LP getLPInfor (int product_id){
        LP lp = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                     "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                     "l.artists, l.record_label, l.tracklist, l.genre, l.release_date " +
                     "FROM products p " +
                     "INNER JOIN lps l ON p.product_id = l.lp_id " +
                     "WHERE p.category = 'LP' AND p.product_id = ? ";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, product_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lp = new LP();
                lp.setProductId(rs.getInt("product_id"));
                lp.setTitle(rs.getString("title"));
                lp.setCategory(rs.getString("category"));
                lp.setValue(rs.getDouble("value"));
                lp.setPrice(rs.getDouble("price"));
                lp.setBarcode(rs.getString("barcode"));
                lp.setDescription(rs.getString("description"));
                lp.setQuantity(rs.getInt("quantity"));
                lp.setWeight(rs.getString("weight")); // Chuyển sang getDouble
                lp.setDimensions(rs.getString("dimensions"));
                lp.setWarehouseEntryDate(rs.getString("warehouse_entry_date")); // Sử dụng getDate
                lp.setArtists(rs.getString("artists"));
                lp.setRecordLabel(rs.getString("record_label"));
                lp.setTracklist(rs.getString("tracklist"));
                lp.setGenre(rs.getString("genre"));
                lp.setReleaseDate(rs.getString("release_date")); // Sử dụng getDate
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return lp;
    }
    
    // Cập nhật LP
    public boolean updateLP(LP lp) {
        String updateProductSql = "UPDATE Products SET title = ?, value = ?, price = ?, barcode = ?, " +
                                  "description = ?, quantity = ?, weight = ?, dimensions = ?, warehouse_entry_date = ? " +
                                  "WHERE product_id = ?";

        String updateLPSql = "UPDATE LPs SET artists = ?, record_label = ?, tracklist = ?, " +
                             "genre = ?, release_date = ? WHERE lp_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(updateProductSql);
             PreparedStatement lpStmt = conn.prepareStatement(updateLPSql)) {

            // Cập nhật Products
            productStmt.setString(1, lp.getTitle());
            productStmt.setDouble(2, lp.getValue());
            productStmt.setDouble(3, lp.getPrice());
            productStmt.setString(4, lp.getBarcode());
            productStmt.setString(5, lp.getDescription());
            productStmt.setInt(6, lp.getQuantity());
            productStmt.setString(7, lp.getWeight());
            productStmt.setString(8, lp.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(lp.getWarehouseEntryDate()));
            productStmt.setInt(10, lp.getProductId());
            productStmt.executeUpdate();

            // Cập nhật LPs
            lpStmt.setString(1, lp.getArtists());
            lpStmt.setString(2, lp.getRecordLabel());
            lpStmt.setString(3, lp.getTracklist());
            lpStmt.setString(4, lp.getGenre());
            if (lp.getReleaseDate() != null && !lp.getReleaseDate().isEmpty()) {
                lpStmt.setDate(5, java.sql.Date.valueOf(lp.getReleaseDate()));
            } else {
                lpStmt.setNull(5, java.sql.Types.DATE);
            }
            lpStmt.setInt(6, lp.getProductId());

            lpStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLP(int productId) {
        String deleteLPSql = "DELETE FROM Lps WHERE lp_id = ?";
        String deleteProductSql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement lpStmt = conn.prepareStatement(deleteLPSql);
             PreparedStatement productStmt = conn.prepareStatement(deleteProductSql)) {

            // Xóa từ bảng Lps trước
            lpStmt.setInt(1, productId);
            lpStmt.executeUpdate();

            // Xóa từ bảng Products sau
            productStmt.setInt(1, productId);
            productStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    // lay danh sach san pham de hien thi tren bang "Panel"
    public List<LP> getAllLPs() {
        List<LP> lps = new ArrayList<>();
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price, p.barcode, " +
                     "p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date, " +
                     "l.artists, l.record_label, l.tracklist, l.genre, l.release_date " +
                     "FROM Products p " +
                     "INNER JOIN LPs l ON p.product_id = l.lp_id " +
                     "WHERE p.category = 'LP'";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                LP lp = new LP(
                    rs.getInt("product_id"),
                    rs.getString("title"),
                    rs.getString("category"), // Đảm bảo bạn chọn cột 'category' trong truy vấn
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
                lps.add(lp);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return lps;
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
    
    public boolean addLP(LP lp) {
        String productSQL = "INSERT INTO Products (title, category, value, price, barcode, description, quantity, weight, dimensions, warehouse_entry_date) " +
                            "VALUES (?, 'LP', ?, ?, ?, ?, ?, ?, ?, ?) RETURNING product_id";
        String lpSQL = "INSERT INTO LPs (lp_id, artists, record_label, tracklist, genre, release_date) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(productSQL);
             PreparedStatement lpStmt = conn.prepareStatement(lpSQL)) {
    
            String barcode = generateUniqueBarcode(conn);
            lp.setBarcode(barcode);
    
            productStmt.setString(1, lp.getTitle());
            productStmt.setDouble(2, lp.getValue());
            productStmt.setDouble(3, lp.getPrice());
            productStmt.setString(4, barcode);
            productStmt.setString(5, lp.getDescription());
            productStmt.setInt(6, lp.getQuantity());
            productStmt.setString(7, lp.getWeight());
            productStmt.setString(8, lp.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(lp.getWarehouseEntryDate()));
    
            ResultSet rs = productStmt.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt(1);
                lp.setProductId(productId);
    
                lpStmt.setInt(1, productId);
                lpStmt.setString(2, lp.getArtists());
                lpStmt.setString(3, lp.getRecordLabel());
                lpStmt.setString(4, lp.getTracklist());
                lpStmt.setString(5, lp.getGenre());
                if (lp.getReleaseDate() != null && !lp.getReleaseDate().isEmpty()) {
                    lpStmt.setDate(6, java.sql.Date.valueOf(lp.getReleaseDate()));
                } else {
                    lpStmt.setNull(6, java.sql.Types.DATE);
                }
    
                return lpStmt.executeUpdate() > 0;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public LP getLPById(int productId) {
        String sql = "SELECT * FROM Products JOIN LPs ON Products.product_id = LPs.product_id WHERE Products.product_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLP(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // so luong LP
    public static int getLPCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM LPs";
    
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
    

    private LP mapResultSetToLP(ResultSet rs) throws SQLException {
        return new LP(
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

    private static LPDAO instance;

    public static LPDAO getInstance() {
        if (instance == null) {
            instance = new LPDAO();
        }
        return instance;
    }

    // Dùng cho test
    public static void setMockInstance(LPDAO mock) {
        instance = mock;
    }
}
