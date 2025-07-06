package Service.Interface;

import Model.User;
import java.util.List;

/**
 * Giảm coupling
 * Tuân thủ Dependency Inversion Principle
 */
public interface IUserService {

    boolean registerUser(User user);
    User authenticateUser(String username, String password);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    boolean setUserBlocked(int userId, boolean blocked);
    boolean resetPassword(int userId, String newPassword);
    User findUserByUsername(String username);
    List<String> getAllRoles();
}
