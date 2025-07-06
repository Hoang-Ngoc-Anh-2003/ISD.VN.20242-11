package Service;

import Model.User;
import Repository.IUserRepository;
import Service.Interface.IPasswordService;
import Service.Interface.IUserService;

import java.util.List;

/**
 * Service xử lý business logic cho User
 * High cohesion - chỉ tập trung vào user
 */
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IPasswordService passwordService;

    public UserService(IUserRepository userRepository, IPasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public boolean registerUser(User user) {
        // Business rules validation
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }

        // Check username tồn tại
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }

        // Set defaults trước khi save
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("customer");
        }

        // Hash password và save trong 1 lần gọi
        String hashedPassword = passwordService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(String username, String plainPassword) {
        if (username == null || plainPassword == null) {
            return null;
        }

        User user = userRepository.findByUsername(username);
        if (user == null || user.isBlocked()) {
            return null;
        }

        boolean isValid = passwordService.verifyPassword(plainPassword, user.getPassword());
        return isValid ? user : null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() <= 0) {
            return false;
        }
        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            return false;
        }
        return userRepository.delete(userId);
    }

    @Override
    public boolean setUserBlocked(int userId, boolean blocked) {
        if (userId <= 0) {
            return false;
        }
        return userRepository.setBlocked(userId, blocked);
    }

    @Override
    public boolean resetPassword(int userId, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        String hashedPassword = passwordService.hashPassword(newPassword);
        return userRepository.updatePassword(userId, hashedPassword);
    }

    @Override
    public User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public List<String> getAllRoles() {
        return userRepository.getAllRoles();
    }
}
