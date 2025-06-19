package Controller;

import Model.DatabaseConnector;
import Model.User;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller quản lý các thao tác liên quan đến người dùng
 */
public class UserController {

    /**
     * Kiểm tra xem tên đăng nhập đã tồn tại chưa
     *
     * @param username Tên đăng nhập cần kiểm tra
     * @return true nếu tên đã tồn tại, false nếu chưa
     */
    public boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra tên đăng nhập: " + e.getMessage());
        }
        return false;
    }

    /**
     * Đăng ký người dùng mới
     *
     * @param user Đối tượng User cần đăng ký
     * @return true nếu đăng ký thành công, false nếu có lỗi hoặc tên đăng nhập đã tồn tại
     */
    public boolean registerUser(User user) {
        if (isUsernameExists(user.getUsername())) {
            return false;
        }

        String sql = "INSERT INTO users(username, password, email, phone, role) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, hashPassword(user.getPassword()));
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPhone());
            pst.setString(5, user.getRole());

            int affected = pst.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = pst.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setUserId(keys.getInt(1));
                    }
                }
                // Gán vai trò mặc định trong bảng user_roles
                setUserRoles(user.getUserId(), java.util.Collections.singletonList(user.getRole()));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng ký người dùng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy thông tin người dùng theo tên đăng nhập
     *
     * @param username Tên đăng nhập
     * @return Đối tượng User nếu tìm thấy, null nếu không tìm thấy
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password, email, phone, role, blocked FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User u = mapUserFromResultSet(rs);
                    u.setRoles(loadUserRoles(conn, u.getUserId()));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin người dùng: " + e.getMessage());
        }
        return null;
    }

    /**
     * Xác thực người dùng khi đăng nhập
     *
     * @param username Tên đăng nhập
     * @param plainPassword Mật khẩu dạng văn bản thường
     * @return true nếu xác thực thành công, false nếu sai thông tin
     */
    public boolean validateUser(String username, String plainPassword) {
        User user = getUserByUsername(username);
        if (user == null) return false;
        String stored = user.getPassword();
        // Hỗ trợ cả mật khẩu văn bản thường hoặc mật khẩu đã hash
        return plainPassword.equals(stored) || hashPassword(plainPassword).equals(stored);
    }

    /**
     * Lấy danh sách tất cả người dùng
     *
     * @return Danh sách các đối tượng User
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, u.role, u.blocked " +
                     "FROM users u ORDER BY u.user_id";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = mapUserFromResultSet(rs);
                user.setRoles(loadUserRoles(conn, user.getUserId()));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách người dùng: " + e.getMessage());
        }

        return users;
    }

    /**
     * Lấy danh sách tất cả vai trò
     */
    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT DISTINCT role FROM users";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                roles.add(rs.getString("role"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách vai trò: " + e.getMessage());
        }
        return roles;
    }

    /**
     * Cập nhật thông tin người dùng
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, phone = ?, blocked = ?, role = ? WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getPhone());
            pst.setBoolean(3, user.isBlocked());
            pst.setString(4, user.getRole());
            pst.setString(5, user.getUsername());
            boolean updated = pst.executeUpdate() > 0;
            if (updated) {
                // Lấy userId để cập nhật roles
                String getIdSql = "SELECT user_id FROM users WHERE username = ?";
                try (PreparedStatement idPst = conn.prepareStatement(getIdSql)) {
                    idPst.setString(1, user.getUsername());
                    try (ResultSet rs = idPst.executeQuery()) {
                        if (rs.next()) {
                            int userId = rs.getInt("user_id");
                            setUserRoles(userId, user.getRoles());
                        }
                    }
                }
            }
            return updated;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật người dùng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Đặt lại mật khẩu người dùng
     *
     * @param userId ID người dùng
     * @param newPassword Mật khẩu mới
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean resetPassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, hashPassword(newPassword));
            pst.setInt(2, userId);

            int affected = pst.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }

        return false;
    }

    /**
     * Xóa người dùng
     *
     * @param userId ID người dùng cần xóa
     * @return true nếu xóa thành công, false nếu có lỗi
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            // Xóa các vai trò của người dùng trước
            String deleteRolesSql = "DELETE FROM user_roles WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(deleteRolesSql)) {
                pst.setInt(1, userId);
                pst.executeUpdate();
            }

            // Sau đó xóa người dùng
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, userId);
                int affected = pst.executeUpdate();

                if (affected == 1) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa người dùng: " + e.getMessage());
        }

        return false;
    }

    /**
     * Khóa hoặc mở khóa tài khoản người dùng
     *
     * @param userId ID người dùng
     * @param blocked true để khóa, false để mở khóa
     * @return true nếu thành công, false nếu có lỗi
     */
    public boolean setBlocked(int userId, boolean blocked) {
        String sql = "UPDATE users SET blocked = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setBoolean(1, blocked);
            pst.setInt(2, userId);

            int affected = pst.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            System.err.println("Lỗi khi " + (blocked ? "khóa" : "mở khóa") + " tài khoản: " + e.getMessage());
        }

        return false;
    }

    /**
     * Kiểm tra tài khoản có bị khóa không
     *
     * @param username Tên đăng nhập
     * @return true nếu tài khoản bị khóa, false nếu không
     */
    public boolean isUserBlocked(String username) {
        String sql = "SELECT blocked FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("blocked");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra trạng thái khóa tài khoản: " + e.getMessage());
        }

        return false;
    }

    /**
     * Đặt vai trò cho người dùng
     *
     * @param userId ID người dùng
     * @param roles Danh sách các vai trò
     */
    private void setUserRoles(int userId, List<String> roles) {
        // Xóa các vai trò hiện tại
        String deleteSql = "DELETE FROM user_roles WHERE user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pst = conn.prepareStatement(deleteSql)) {
                pst.setInt(1, userId);
                pst.executeUpdate();
            }

            // Thêm vai trò mới
            String insertSql = "INSERT INTO user_roles(user_id, role_id) " +
                              "SELECT ?, role_id FROM roles WHERE role_name = ?";

            try (PreparedStatement pst = conn.prepareStatement(insertSql)) {
                for (String role : roles) {
                    pst.setInt(1, userId);
                    pst.setString(2, role);
                    pst.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đặt vai trò cho người dùng: " + e.getMessage());
        }
    }

    /**
     * Mã hóa mật khẩu
     *
     * @param plainPassword Mật khẩu dạng văn bản thường
     * @return Mật khẩu đã được mã hóa
     */
    private String hashPassword(String plainPassword) {
        // Sử dụng phương thức hash
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            System.err.println("Lỗi khi mã hóa mật khẩu: " + e.getMessage());
            return plainPassword; // Fallback nếu có lỗi
        }
    }

    /**
     * Tải danh sách vai trò của người dùng
     *
     * @param conn Kết nối cơ sở dữ liệu
     * @param userId ID người dùng
     * @return Danh sách vai trò của người dùng
     */
    private List<String> loadUserRoles(Connection conn, int userId) {
        List<String> roleList = new ArrayList<>();
        String roleSql = "SELECT r.role_name FROM user_roles ur JOIN roles r ON ur.role_id=r.role_id WHERE ur.user_id = ?";
        try (PreparedStatement rolePst = conn.prepareStatement(roleSql)) {
            rolePst.setInt(1, userId);
            try (ResultSet rs2 = rolePst.executeQuery()) {
                while (rs2.next()) {
                    roleList.add(rs2.getString("role_name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải vai trò người dùng: " + e.getMessage());
        }
        return roleList;
    }

    /**
     * Ánh xạ kết quả từ ResultSet sang đối tượng User
     *
     * @param rs ResultSet chứa dữ liệu người dùng
     * @return Đối tượng User
     * @throws SQLException Nếu có lỗi xảy ra khi truy xuất dữ liệu
     */
    private User mapUserFromResultSet(ResultSet rs) throws SQLException {
        User u = new User(
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("role")
        );
        u.setBlocked(rs.getBoolean("blocked"));
        return u;
    }
}
