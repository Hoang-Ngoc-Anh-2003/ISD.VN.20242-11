package Controller;

import DAO.ProductDAO;
import Model.DatabaseConnector;
import Model.DVD;
import Model.ProductResultSetUtil;
import Model.QueryUtils;
import java.sql.*;
import java.util.List;

/**
 * Controller quản lý các thao tác liên quan đến sản phẩm DVD
 */
public class DVDController extends ProductController {

    /**
     * Lấy tất cả thông tin DVD từ cơ sở dữ liệu
     *
     * @return Danh sách các DVD
     */
    public List<DVD> getAllDVDs() {
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price, p.barcode, " +
                     "p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date, " +
                     "d.disc_type, d.director, d.runtime, d.studio, d.language, " +
                     "d.subtitles, d.release_date, d.genre " +
                     "FROM Products p " +
                     "INNER JOIN DVDs d ON p.product_id = d.dvd_id " +
                     "WHERE p.category = 'DVD'";
        return QueryUtils.executeQueryList(sql, this::createDVDFromResultSet);
    }

    /**
     * Lấy thông tin chi tiết của một DVD theo ID
     *
     * @param dvdId ID của DVD cần truy vấn
     * @return Đối tượng DVD chứa thông tin chi tiết, hoặc null nếu không tìm thấy
     */
    public DVD getDVDInfo(int dvdId) {
        DVD dvd = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                     "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                     "d.disc_type, d.director, d.runtime, d.studio, d.language, d.subtitles, d.release_date, d.genre " +
                     "FROM products p " +
                     "INNER JOIN dvds d ON p.product_id = d.dvd_id " +
                     "WHERE p.category = 'DVD' AND p.product_id = ? ";

        try(Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, dvdId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dvd = createDVDFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin DVD: " + e.getMessage());
        }

        return dvd;
    }

    /**
     * Thêm DVD mới vào cơ sở dữ liệu
     *
     * @param dvd Đối tượng DVD cần thêm
     * @return true nếu thêm thành công, false nếu có lỗi
     */
    public boolean addDVD(DVD dvd) {
        Connection conn = null;
        PreparedStatement pstmtDVD = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // Ensure the category is always set to "DVD" (uppercase) to satisfy the DB constraint
            dvd.setCategory("DVD");

            // Sử dụng ProductDAO để thêm vào bảng Products
            int productId = ProductDAO.insertProduct(dvd, conn);

            // Sau đó thêm vào bảng DVDs
            String sqlDVD = "INSERT INTO DVDs (dvd_id, disc_type, director, runtime, studio, language, " +
                            "subtitles, release_date, genre) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmtDVD = conn.prepareStatement(sqlDVD);
            pstmtDVD.setInt(1, productId);
            pstmtDVD.setString(2, dvd.getDiscType());
            pstmtDVD.setString(3, dvd.getDirector());
            pstmtDVD.setInt(4, dvd.getRuntime());
            pstmtDVD.setString(5, dvd.getStudio());
            pstmtDVD.setString(6, dvd.getLanguage());
            pstmtDVD.setString(7, dvd.getSubtitles());
            pstmtDVD.setObject(8, dvd.getReleaseDate() == null || dvd.getReleaseDate().isEmpty() ? null : java.sql.Date.valueOf(dvd.getReleaseDate()));
            pstmtDVD.setString(9, dvd.getGenre());

            pstmtDVD.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi khi thêm DVD: " + e.getMessage());
        } finally {
            try {
                if (pstmtDVD != null) pstmtDVD.close();
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
     * Cập nhật thông tin DVD trong cơ sở dữ liệu
     *
     * @param dvd Đối tượng DVD cần cập nhật
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean updateDVD(DVD dvd) {
        Connection conn = null;
        PreparedStatement pstmtDVD = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // Cập nhật bảng Products
            ProductDAO.updateProduct(dvd, conn);

            // Cập nhật bảng DVDs
            String sqlDVD = "UPDATE DVDs SET disc_type = ?, director = ?, runtime = ?, studio = ?, " +
                            "language = ?, subtitles = ?, release_date = ?, genre = ? " +
                            "WHERE dvd_id = ?";

            pstmtDVD = conn.prepareStatement(sqlDVD);
            pstmtDVD.setString(1, dvd.getDiscType());
            pstmtDVD.setString(2, dvd.getDirector());
            pstmtDVD.setInt(3, dvd.getRuntime());
            pstmtDVD.setString(4, dvd.getStudio());
            pstmtDVD.setString(5, dvd.getLanguage());
            pstmtDVD.setString(6, dvd.getSubtitles());
            pstmtDVD.setObject(7, dvd.getReleaseDate() == null || dvd.getReleaseDate().isEmpty() ? null : java.sql.Date.valueOf(dvd.getReleaseDate()));
            pstmtDVD.setString(8, dvd.getGenre());
            pstmtDVD.setInt(9, dvd.getProductId());

            pstmtDVD.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi khi cập nhật DVD: " + e.getMessage());
        } finally {
            try {
                if (pstmtDVD != null) pstmtDVD.close();
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
     * Xóa DVD theo ID
     *
     * @param dvdId ID của DVD cần xóa
     * @return true nếu xóa thành công, false nếu có lỗi
     */
    public boolean deleteDVD(int dvdId) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            String sqlDVD = "DELETE FROM DVDs WHERE dvd_id = ?";
            String sqlProduct = "DELETE FROM Products WHERE product_id = ?";
            ControllerUtil.deleteById(conn, sqlDVD, sqlProduct, dvdId);
            conn.commit();
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Lỗi khi rollback: " + ex.getMessage()); }
            }
            System.err.println("Lỗi khi xóa DVD: " + e.getMessage());
        } finally {
            ControllerUtil.closeQuietly(conn);
        }
        return success;
    }

    /**
     * Tạo đối tượng DVD từ ResultSet
     *
     * @param rs ResultSet chứa dữ liệu DVD
     * @return Đối tượng DVD
     * @throws SQLException Nếu có lỗi khi truy xuất dữ liệu
     */
    private DVD createDVDFromResultSet(ResultSet rs) throws SQLException {
        DVD dvd = new DVD();
        ProductResultSetUtil.fillProductFieldsFromResultSet(dvd, rs);
        dvd.setDiscType(rs.getString("disc_type"));
        dvd.setDirector(rs.getString("director"));
        dvd.setRuntime(rs.getInt("runtime"));
        dvd.setStudio(rs.getString("studio"));
        dvd.setLanguage(rs.getString("language"));
        dvd.setSubtitles(rs.getString("subtitles"));
        dvd.setReleaseDate(rs.getString("release_date"));
        dvd.setGenre(rs.getString("genre"));
        return dvd;
    }
}
