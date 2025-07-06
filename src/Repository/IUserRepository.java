package Repository;

import Model.User;
import java.util.List;

/**
 * Interface cho User Repository - Data Access Layer
 * Tách biệt hoàn toàn với business logic
 */
public interface IUserRepository {
    boolean save(User user);
    User findByUsername(String username);
    User findById(int userId);
    List<User> findAll();
    boolean update(User user);
    boolean delete(int userId);
    boolean existsByUsername(String username);
    boolean setBlocked(int userId, boolean blocked);
    boolean updatePassword(int userId, String hashedPassword);
    List<String> getUserRoles(int userId);
    void setUserRoles(int userId, List<String> roles);
    List<String> getAllRoles();
}
