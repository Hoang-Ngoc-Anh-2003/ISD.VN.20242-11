package Controller;

import DAO.ProductDAO;
import Model.DatabaseConnector;
import Model.LP;
import Model.ProductResultSetUtil;
import Model.QueryUtils;
import java.sql.*;
import java.util.List;

/**
 * Controller quản lý các thao tác liên quan đến đĩa than (LP)
 */
public class LPController extends ProductController {

    /**
     * Lấy danh sách tất cả đĩa than từ cơ sở dữ liệu
     *
     * @return Danh sách các đĩa than
     */
    public List<LP> getAllLPs() {
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price, p.barcode, " +
                     "p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date, " +
                     "l.artists, l.record_label, l.tracklist, l.genre, l.release_date " +
                     "FROM products p " +
                     "INNER JOIN lps l ON p.product_id = l.lp_id " +
                     "WHERE p.category = 'LP'";
        return QueryUtils.executeQueryList(sql, this::createLPFromResultSet);
    }

    /**
     * Lấy thông tin chi tiết của một đĩa than theo ID
     *
     * @param lpId ID của đĩa than cần truy vấn
     * @return Đối tượng LP chứa thông tin chi tiết, hoặc null nếu không tìm thấy
     */
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

    /**
     * Thêm đĩa than mới vào cơ sở dữ liệu
     *
     * @param lp Đối tượng LP cần thêm
     * @return true nếu thêm thành công, false nếu có lỗi
     */
    public boolean addLP(LP lp) {
        Connection conn = null;
        PreparedStatement pstmtLP = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // Ensure the category is always set to "LP" (uppercase) to satisfy the DB constraint
            lp.setCategory("LP");

            // Sử dụng ProductDAO để thêm vào bảng Products
            int productId = ProductDAO.insertProduct(lp, conn);

            // Sau đó thêm vào bảng LPs
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

    /**
     * Cập nhật thông tin đĩa than trong cơ sở dữ liệu
     *
     * @param lp Đối tượng LP cần cập nhật
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
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

    /**
     * Xóa đĩa than theo ID
     *
     * @param lpId ID của đĩa than cần xóa
     * @return true nếu xóa thành công, false nếu có lỗi
     */
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

    /**
     * Tạo đối tượng LP từ ResultSet
     *
     * @param rs ResultSet chứa dữ liệu đĩa than
     * @return Đối tượng LP
     * @throws SQLException Nếu có lỗi khi truy xuất dữ liệu
     */
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
