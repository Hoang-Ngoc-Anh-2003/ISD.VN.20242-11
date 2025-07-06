package Repository;

import Model.DatabaseConnector;
import Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation cho User
 * Low coupling - chỉ phụ thuộc vào database
 * High cohesion - chỉ tập trung vào data access
 */
@SuppressWarnings("ALL")
public class UserRepository implements IUserRepository {

    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users(username, password, email, phone, role, blocked) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword()); // Already hashed by service
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPhone());
            String defaultRole = user.getRole() != null ? user.getRole() : "customer";
            pst.setString(5, defaultRole);
            pst.setBoolean(6, false); // New users not blocked by default

            int affected = pst.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = pst.getGeneratedKeys()) {
                    if (keys.next()) {
                        int userId = keys.getInt(1);
                        user.setUserId(userId);

                        // Also add the default role to user_roles table
                        String roleInsertSql = "INSERT INTO user_roles(user_id, role_id) " +
                                              "SELECT ?, role_id FROM roles WHERE role_name = ?";
                        try (PreparedStatement rolePst = conn.prepareStatement(roleInsertSql)) {
                            rolePst.setInt(1, userId);
                            rolePst.setString(2, defaultRole);
                            rolePst.executeUpdate();
                        }

                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu người dùng: " + e.getMessage());
        }
        return false;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT user_id, username, password, email, phone, role, blocked FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = mapUserFromResultSet(rs);
                    user.setRoles(getUserRoles(user.getUserId()));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm người dùng: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User findById(int userId) {
        String sql = "SELECT user_id, username, password, email, phone, role, blocked FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = mapUserFromResultSet(rs);
                    user.setRoles(getUserRoles(userId));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm người dùng theo ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password, email, phone, role, blocked FROM users ORDER BY user_id";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = mapUserFromResultSet(rs);
                user.setRoles(getUserRoles(user.getUserId()));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách người dùng: " + e.getMessage());
        }
        return users;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phone = ?, blocked = ?, role = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPhone());
            pst.setBoolean(4, user.isBlocked());
            pst.setString(5, user.getRole());
            pst.setInt(6, user.getUserId());

            boolean updated = pst.executeUpdate() > 0;
            if (updated && user.getRoles() != null) {
                setUserRoles(user.getUserId(), user.getRoles());
            }
            return updated;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật người dùng: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int userId) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            // Xóa roles trước
            String deleteRolesSql = "DELETE FROM user_roles WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(deleteRolesSql)) {
                pst.setInt(1, userId);
                pst.executeUpdate();
            }

            // Xóa user
            String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(deleteUserSql)) {
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

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra username: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean setBlocked(int userId, boolean blocked) {
        String sql = "UPDATE users SET blocked = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setBoolean(1, blocked);
            pst.setInt(2, userId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật trạng thái blocked: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, hashedPassword);
            pst.setInt(2, userId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<String> getUserRoles(int userId) {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT r.role_name FROM user_roles ur " +
                    "JOIN roles r ON ur.role_id = r.role_id " +
                    "WHERE ur.user_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("role_name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy roles của user: " + e.getMessage());
        }
        return roles;
    }

    @Override
    public void setUserRoles(int userId, List<String> roles) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            // Xóa roles hiện tại
            String deleteSql = "DELETE FROM user_roles WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(deleteSql)) {
                pst.setInt(1, userId);
                pst.executeUpdate();
            }

            // Thêm roles mới
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
            System.err.println("Lỗi khi set roles cho user: " + e.getMessage());
        }
    }

    private User mapUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("role")
        );
        // Cập nhật trường blocked từ database
        user.setBlocked(rs.getBoolean("blocked"));
        return user;
    }

    @Override
    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT DISTINCT role FROM users";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("role");
                if (role != null && !role.trim().isEmpty()) {
                    roles.add(role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách vai trò: " + e.getMessage());
        }

        return roles;
    }

}
