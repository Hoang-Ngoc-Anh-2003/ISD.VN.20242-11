package Controller;

import DAO.ProductDAO;
import Model.DatabaseConnector;
import Model.CD;
import Model.QueryUtils;
import Model.ProductResultSetUtil;
import java.sql.*;
import java.util.List;

/**
 * Controller quản lý các thao tác liên quan đến đĩa CD
 */
public class CDController extends ProductController {

    /**
     * Lấy danh sách tất cả CD từ cơ sở dữ liệu
     *
     * @return Danh sách các đĩa CD
     */
    public List<CD> getAllCDs() {
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price, p.barcode, " +
                     "p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date, " +
                     "c.artists, c.record_label, c.tracklist, c.genre, c.release_date " +
                     "FROM products p " +
                     "INNER JOIN cds c ON p.product_id = c.cd_id " +
                     "WHERE p.category = 'CD'";
        return QueryUtils.executeQueryList(sql, this::createCDFromResultSet);
    }

    /**
     * Lấy thông tin chi tiết của một CD theo ID
     *
     * @param cdId ID của CD cần truy vấn
     * @return Đối tượng CD chứa thông tin chi tiết, hoặc null nếu không tìm thấy
     */
    public CD getCDInfo(int cdId) {
        CD cd = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                     "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                     "c.artists, c.record_label, c.tracklist, c.genre, c.release_date " +
                     "FROM products p " +
                     "INNER JOIN cds c ON p.product_id = c.cd_id " +
                     "WHERE p.category = 'CD' AND p.product_id = ? ";

        try(Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, cdId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cd = createCDFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin CD: " + e.getMessage());
        }

        return cd;
    }

    /**
     * Thêm CD mới vào cơ sở dữ liệu
     *
     * @param cd Đối tượng CD cần thêm
     * @return true nếu thêm thành công, false nếu có lỗi
     */
    public boolean addCD(CD cd) {
        Connection conn = null;
        PreparedStatement pstmtCD = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // Ensure the Product category is always set to "CD" (uppercase) to satisfy DB constraint
            cd.setCategory("CD");

            // Sử dụng ProductDAO để thêm vào bảng Products
            int productId = ProductDAO.insertProduct(cd, conn);

            // Sau đó thêm vào bảng CDs
            String sqlCD = "INSERT INTO CDs (artists, record_label, tracklist, genre, release_date, cd_id) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";

            pstmtCD = conn.prepareStatement(sqlCD);
            ControllerUtil.setProductFields(
                pstmtCD,
                cd.getArtists(),
                cd.getRecordLabel(),
                cd.getTracklist(),
                cd.getGenre(),
                cd.getReleaseDate(),
                productId
            );

            pstmtCD.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi khi thêm CD: " + e.getMessage());
        } finally {
            try {
                if (pstmtCD != null) pstmtCD.close();
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
     * Cập nhật thông tin CD trong cơ sở dữ liệu
     *
     * @param cd Đối tượng CD cần cập nhật
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean updateCD(CD cd) {
        Connection conn = null;
        PreparedStatement pstmtCD = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // Cập nhật bảng Products
            ProductDAO.updateProduct(cd, conn);

            // Cập nhật bảng CDs
            String sqlCD = "UPDATE CDs SET artists = ?, record_label = ?, tracklist = ?, genre = ?, release_date = ? " +
                          "WHERE cd_id = ?";

            pstmtCD = conn.prepareStatement(sqlCD);
            ControllerUtil.setProductFields(
                pstmtCD,
                cd.getArtists(),
                cd.getRecordLabel(),
                cd.getTracklist(),
                cd.getGenre(),
                cd.getReleaseDate(),
                cd.getProductId()
            );
            pstmtCD.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi khi cập nhật CD: " + e.getMessage());
        } finally {
            try {
                if (pstmtCD != null) pstmtCD.close();
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
     * Xóa CD theo ID
     *
     * @param cdId ID của CD cần xóa
     * @return true nếu xóa thành công, false nếu có lỗi
     */
    public boolean deleteCD(int cdId) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            String sqlCD = "DELETE FROM CDs WHERE cd_id = ?";
            String sqlProduct = "DELETE FROM Products WHERE product_id = ?";
            ControllerUtil.deleteById(conn, sqlCD, sqlProduct, cdId);
            conn.commit();
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Lỗi khi rollback: " + ex.getMessage()); }
            }
            System.err.println("Lỗi khi xóa CD: " + e.getMessage());
        } finally {
            ControllerUtil.closeQuietly(conn);
        }
        return success;
    }

    /**
     * Tạo đối tượng CD từ ResultSet
     *
     * @param rs ResultSet chứa dữ liệu CD
     * @return Đối tượng CD
     * @throws SQLException Nếu có lỗi khi truy xuất dữ liệu
     */
    private CD createCDFromResultSet(ResultSet rs) throws SQLException {
        CD cd = new CD();
        ProductResultSetUtil.fillProductFieldsFromResultSet(cd, rs);
        setMediaFields(cd, rs);
        return cd;
    }

    /**
     * Gán các trường media cho CD/LP từ ResultSet (dùng chung cho các controller media)
     */
    private void setMediaFields(CD cd, ResultSet rs) throws SQLException {
        String[] fields = ControllerUtil.getMediaFieldsFromResultSet(rs);
        cd.setArtists(fields[0]);
        cd.setRecordLabel(fields[1]);
        cd.setTracklist(fields[2]);
        cd.setGenre(fields[3]);
        cd.setReleaseDate(fields[4]);
    }
}
