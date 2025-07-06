package Controller;

import Model.User;
import Service.Interface.IUserService;
import Service.UserService;
import Service.PasswordService;
import Repository.UserRepository;

import java.util.List;

/**
 * Low coupling - dependency injection thông qua constructor
 * High cohesion - chỉ làm controller logic
 */
public class UserController {
    private final IUserService userService;

    // Constructor injection để giảm coupling
    public UserController() {
        UserRepository userRepository = new UserRepository();
        PasswordService passwordService = new PasswordService();
        this.userService = new UserService(userRepository, passwordService);
    }

    // Constructor cho testing và dependency injection
    @SuppressWarnings("unused") // Dành cho testing và future use
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    public boolean registerUser(User user) {
        try {
            return userService.registerUser(user);
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi đăng ký: " + e.getMessage());
            return false;
        }
    }

    public User authenticateUser(String username, String password) {
        try {
            return userService.authenticateUser(username, password);
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi xác thực: " + e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi lấy danh sách: " + e.getMessage());
            return List.of();
        }
    }

    public boolean updateUser(User user) {
        try {
            return userService.updateUser(user);
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi cập nhật: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return userService.deleteUser(userId);
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi xóa: " + e.getMessage());
            return false;
        }
    }

    public boolean resetPassword(int userId, String newPassword) {
        try {
            return userService.resetPassword(userId, newPassword);
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi reset mật khẩu: " + e.getMessage());
            return false;
        }
    }

    public boolean isUserBlocked(String username) {
        try {
            User user = userService.findUserByUsername(username);
            return user != null && user.isBlocked();
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi kiểm tra khóa: " + e.getMessage());
            return false;
        }
    }

    public List<String> getAllRoles() {
        try {
            return userService.getAllRoles();
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi lấy danh sách vai trò: " + e.getMessage());
            return List.of();
        }
    }

    public User findUserByUsername(String username) {
        try {
            return userService.findUserByUsername(username);
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi tìm user: " + e.getMessage());
            return null;
        }
    }

    public boolean setBlocked(int userId, boolean blocked) {
        try {
            return userService.setUserBlocked(userId, blocked);
        } catch (Exception e) {
            System.err.println("Lỗi trong controller khi set blocked: " + e.getMessage());
            return false;
        }
    }
}
