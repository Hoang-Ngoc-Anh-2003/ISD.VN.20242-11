package DAO;

import Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import DAO.PasswordUtil;

public class UserDAO {
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(User user) {
        if (usernameExists(user.getUsername())) {
            return false;
        }
        String sql = "INSERT INTO users(username,password,email,phone,role) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, PasswordUtil.hash(user.getPassword()));
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
                // Assign default role in user_roles table
                setUserRoles(user.getUserId(), java.util.Collections.singletonList(user.getRole()));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT user_id,username,password,email,phone,role,blocked FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User u = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("role")
                    );
                    u.setBlocked(rs.getBoolean("blocked"));
                    // Load all roles for this user
                    List<String> roleList = new ArrayList<>();
                    String roleSql = "SELECT r.role_name FROM user_roles ur JOIN roles r ON ur.role_id=r.role_id WHERE ur.user_id = ?";
                    try (PreparedStatement rolePst = conn.prepareStatement(roleSql)) {
                        rolePst.setInt(1, u.getUserId());
                        try (ResultSet rs2 = rolePst.executeQuery()) {
                            while (rs2.next()) {
                                roleList.add(rs2.getString("role_name"));
                            }
                        }
                    }
                    u.setRoles(roleList);
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateUser(String username, String plainPassword) {
        User user = getUserByUsername(username);
        if (user == null) return false;
        String stored = user.getPassword();
        // support legacy plain-text or new hashed passwords for any user
        return plainPassword.equals(stored) || PasswordUtil.hash(plainPassword).equals(stored);
    }

    // Fetch all users with their roles and blocked status
    public List<Model.User> getAllUsers() {
        String sql = "SELECT u.user_id,u.username,u.password,u.email,u.phone,u.blocked, array_agg(r.role_name) AS roles " +
                "FROM users u LEFT JOIN user_roles ur ON u.user_id=ur.user_id " +
                "LEFT JOIN roles r ON ur.role_id=r.role_id GROUP BY u.user_id";
        List<Model.User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                boolean blocked = rs.getBoolean("blocked");
                Array rolesArr = rs.getArray("roles");
                String[] roles = rolesArr != null ? (String[]) rolesArr.getArray() : new String[0];
                users.add(new Model.User(id, username, password, email, phone, Arrays.asList(roles), blocked));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	    return users;
    }

    // Fetch all available role names
    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT role_name FROM roles";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                roles.add(rs.getString("role_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // Block or unblock a user
    public boolean setBlocked(int userId, boolean blocked) {
        String sql = "UPDATE users SET blocked = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setBoolean(1, blocked);
            pst.setInt(2, userId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Reset a user's password to the given new password (hashed)
    public boolean resetPassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        String hashed = PasswordUtil.hash(newPassword);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, hashed);
            pst.setInt(2, userId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a user by ID
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, userId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Set roles for a user (replace existing roles)
    public boolean setUserRoles(int userId, List<String> roleNames) {
        String delSql = "DELETE FROM user_roles WHERE user_id = ?";
        String insSql = "INSERT INTO user_roles(user_id, role_id) VALUES (?, (SELECT role_id FROM roles WHERE role_name = ?))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement delPst = conn.prepareStatement(delSql);
             PreparedStatement insPst = conn.prepareStatement(insSql)) {
            conn.setAutoCommit(false);
            delPst.setInt(1, userId);
            delPst.executeUpdate();
            for (String role : roleNames) {
                insPst.setInt(1, userId);
                insPst.setString(2, role);
                insPst.addBatch();
            }
            insPst.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { DatabaseConnection.getConnection().rollback(); } catch (Exception ex) {}
        }
        return false;
    }

    // Update user profile and roles
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phone = ?, blocked = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPhone());
            pst.setBoolean(4, user.isBlocked());
            pst.setInt(5, user.getUserId());
            int updated = pst.executeUpdate();
            if (updated == 1) {
                // update roles
                setUserRoles(user.getUserId(), user.getRoles());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

