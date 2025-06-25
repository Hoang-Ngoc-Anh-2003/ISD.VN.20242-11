package aimsfinal.Controller;

import aimsfinal.DAO.UserDAO;
import aimsfinal.Model.User;
import java.util.List;

public class UserController {
    private UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    public User register(String username, String password, String email, String fullName, String phone, String address) {
        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            return null;
        }

        // Check if username or email already exists
        if (userDAO.isUsernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userDAO.isEmailExists(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In a real application, this should be hashed
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);

        boolean success = userDAO.create(user);
        return success ? user : null;
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }

        boolean authenticated = userDAO.authenticate(username, password);
        return authenticated ? userDAO.getByUsername(username) : null;
    }

    public User getUserById(int id) {
        return userDAO.getById(id);
    }

    public User getUserByUsername(String username) {
        return userDAO.getByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public boolean updateUser(User user) {
        if (user == null || user.getId() <= 0) {
            return false;
        }
        return userDAO.update(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }

    public List<String> getUserRoles(int userId) {
        User user = userDAO.getById(userId);
        return user != null ? user.getRoles() : null;
    }

    public boolean addUserRole(int userId, String role) {
        User user = userDAO.getById(userId);
        if (user != null) {
            List<String> roles = user.getRoles();
            if (!roles.contains(role)) {
                roles.add(role);
                user.setRoles(roles);
                return userDAO.update(user);
            }
        }
        return false;
    }

    public boolean removeUserRole(int userId, String role) {
        User user = userDAO.getById(userId);
        if (user != null) {
            List<String> roles = user.getRoles();
            if (roles.remove(role)) {
                user.setRoles(roles);
                return userDAO.update(user);
            }
        }
        return false;
    }

    public boolean isAdmin(int userId) {
        User user = userDAO.getById(userId);
        return user != null && user.getRoles().contains("admin");
    }

    public boolean isManager(int userId) {
        User user = userDAO.getById(userId);
        return user != null && user.getRoles().contains("manager");
    }

    public boolean isCustomer(int userId) {
        User user = userDAO.getById(userId);
        return user != null && user.getRoles().contains("customer");
    }

    // Additional method for GUI compatibility
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        try {
            User user = userDAO.getById(userId);
            if (user != null && userDAO.authenticate(user.getUsername(), currentPassword)) {
                user.setPassword(newPassword);
                return userDAO.update(user);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
