package aimsfinal.DAO;

import aimsfinal.Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        
        // Insert user without role field
        String sql = "INSERT INTO users(username, password, email, phone, full_name, blocked) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, PasswordUtil.hash(user.getPassword()));
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPhone());
            pst.setString(5, user.getFullName());
            pst.setBoolean(6, user.isBlocked());
            
            int affected = pst.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = pst.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setUserId(keys.getInt(1));
                    }
                }
                // Assign default role in user_roles table
                List<String> roles = user.getRoles();
                if (roles == null || roles.isEmpty()) {
                    roles = Arrays.asList("customer"); // Default role
                }
                setUserRoles(user.getUserId(), roles);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password, email, phone, full_name, blocked FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setFullName(rs.getString("full_name"));
                    u.setBlocked(rs.getBoolean("blocked"));
                    
                    // Load all roles for this user
                    List<String> roleList = getUserRoles(u.getUserId());
                    u.setRoles(roleList);
                    
                    // For backward compatibility, set the first role as the main role
                    if (!roleList.isEmpty()) {
                        u.setRole(roleList.get(0));
                    }
                    
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
        // Use PasswordUtil.check for proper password verification
        return PasswordUtil.check(plainPassword, stored);
    }

    // Get roles for a specific user
    public List<String> getUserRoles(int userId) {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT r.role_name FROM user_roles ur JOIN roles r ON ur.role_id = r.role_id WHERE ur.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("role_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // Fetch all users with their roles and blocked status
    public List<aimsfinal.Model.User> getAllUsers() {
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, u.full_name, u.blocked, " +
                    "array_agg(r.role_name) AS roles " +
                    "FROM users u LEFT JOIN user_roles ur ON u.user_id = ur.user_id " +
                    "LEFT JOIN roles r ON ur.role_id = r.role_id " +
                    "GROUP BY u.user_id, u.username, u.password, u.email, u.phone, u.full_name, u.blocked";
        List<aimsfinal.Model.User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String fullName = rs.getString("full_name");
                boolean blocked = rs.getBoolean("blocked");
                Array rolesArr = rs.getArray("roles");
                String[] roles = rolesArr != null ? (String[]) rolesArr.getArray() : new String[0];
                
                User user = new User(id, username, password, email, phone, Arrays.asList(roles), blocked);
                user.setFullName(fullName);
                users.add(user);
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
        }
        return false;
    }

    // Update user information
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phone = ?, full_name = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getFullName());
            pst.setInt(5, user.getUserId());
            
            int affected = pst.executeUpdate();
            if (affected == 1) {
                // Update roles if provided
                if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                    setUserRoles(user.getUserId(), user.getRoles());
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if username exists
    public boolean isUsernameExists(String username) {
        return usernameExists(username);
    }

    // Check if email exists
    public boolean isEmailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Create user (alias for registerUser)
    public boolean create(User user) {
        return registerUser(user);
    }

    // Authenticate user (alias for validateUser)
    public boolean authenticate(String username, String password) {
        return validateUser(username, password);
    }

    // Get user by ID
    public User getById(int id) {
        String sql = "SELECT user_id, username, password, email, phone, full_name, blocked FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setFullName(rs.getString("full_name"));
                    u.setBlocked(rs.getBoolean("blocked"));
                    
                    // Load roles
                    List<String> roleList = getUserRoles(u.getUserId());
                    u.setRoles(roleList);
                    
                    // For backward compatibility
                    if (!roleList.isEmpty()) {
                        u.setRole(roleList.get(0));
                    }
                    
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get user by username (alias for getUserByUsername)
    public User getByUsername(String username) {
        return getUserByUsername(username);
    }

    // Get all users (alias for getAllUsers)
    public List<User> getAll() {
        return getAllUsers();
    }

    // Update user (alias for updateUser)
    public boolean update(User user) {
        return updateUser(user);
    }

    // Delete user (alias for deleteUser)
    public boolean delete(int id) {
        return deleteUser(id);
    }
}

