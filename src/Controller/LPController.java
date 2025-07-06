package Controller;

import DAO.ProductDAO;
import Model.DatabaseConnector;
import Model.LP;
import Model.ProductResultSetUtil;
import Model.QueryUtils;
import java.sql.*;
import java.util.List;

public class LPController extends ProductController {

    public List<LP> getAllLPs() {
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price, p.barcode, " +
                     "p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date, " +
                     "l.artists, l.record_label, l.tracklist, l.genre, l.release_date " +
                     "FROM products p " +
                     "INNER JOIN lps l ON p.product_id = l.lp_id " +
                     "WHERE p.category = 'LP' " +
                     "ORDER BY p.product_id ASC";
        return QueryUtils.executeQueryList(sql, this::createLPFromResultSet);
    }

    public LP getLPInfo(int lpId) {
        LP lp = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                "l.artists, l.record_label, l.tracklist, l.genre, l.release_date " +
                "FROM products p " +
                "INNER JOIN lps l ON p.product_id = l.lp_id " +
                "WHERE p.category = 'LP' AND p.product_id = ? ";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lpId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lp = createLPFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin đĩa than: " + e.getMessage());
        }

        return lp;
    }

    public boolean addLP(LP lp) {
        Connection conn = null;
        PreparedStatement pstmtLP = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            lp.setCategory("LP");

            int productId = ProductDAO.insertProduct(lp, conn);

            String sqlLP = "INSERT INTO LPs (lp_id, artists, record_label, tracklist, genre, release_date) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
            pstmtLP = conn.prepareStatement(sqlLP);
            pstmtLP.setInt(1, productId);
            pstmtLP.setString(2, lp.getArtists());
            pstmtLP.setString(3, lp.getRecordLabel());
            pstmtLP.setString(4, lp.getTracklist());
            pstmtLP.setString(5, lp.getGenre());
            pstmtLP.setObject(6, lp.getReleaseDate() == null || lp.getReleaseDate().isEmpty() ? null : java.sql.Date.valueOf(lp.getReleaseDate()));

            pstmtLP.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi khi thêm đĩa than: " + e.getMessage());
        } finally {
            try {
                if (pstmtLP != null) pstmtLP.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }

        return success;
    }

    public boolean updateLP(LP lp) {
        Connection conn = null;
        PreparedStatement pstmtLP = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // Cập nhật bảng Products
            ProductDAO.updateProduct(lp, conn);

            // Cập nhật bảng LPs
            String sqlLP = "UPDATE LPs SET artists = ?, record_label = ?, tracklist = ?, genre = ?, release_date = ? " +
                          "WHERE lp_id = ?";

            pstmtLP = conn.prepareStatement(sqlLP);
            pstmtLP.setString(1, lp.getArtists());
            pstmtLP.setString(2, lp.getRecordLabel());
            pstmtLP.setString(3, lp.getTracklist());
            pstmtLP.setString(4, lp.getGenre());
            pstmtLP.setObject(5, lp.getReleaseDate() == null || lp.getReleaseDate().isEmpty() ? null : java.sql.Date.valueOf(lp.getReleaseDate()));
            pstmtLP.setInt(6, lp.getProductId());

            pstmtLP.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi khi cập nhật đĩa than: " + e.getMessage());
        } finally {
            try {
                if (pstmtLP != null) pstmtLP.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }

        return success;
    }

    public boolean deleteLP(int lpId) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            String sqlLP = "DELETE FROM LPs WHERE lp_id = ?";
            String sqlProduct = "DELETE FROM Products WHERE product_id = ?";
            ControllerUtil.deleteById(conn, sqlLP, sqlProduct, lpId);
            conn.commit();
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Lỗi khi rollback: " + ex.getMessage()); }
            }
            System.err.println("Lỗi khi xóa LP: " + e.getMessage());
        } finally {
            ControllerUtil.closeQuietly(conn);
        }
        return success;
    }

    private LP createLPFromResultSet(ResultSet rs) throws SQLException {
        LP lp = new LP();
        ProductResultSetUtil.fillProductFieldsFromResultSet(lp, rs);
        String[] fields = ControllerUtil.getMediaFieldsFromResultSet(rs);
        lp.setArtists(fields[0]);
        lp.setRecordLabel(fields[1]);
        lp.setTracklist(fields[2]);
        lp.setGenre(fields[3]);
        lp.setReleaseDate(fields[4]);
        return lp;
    }
}
