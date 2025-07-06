package Service.Interface;

/**
 * Interface cho Password Service - tách riêng responsibility
 * High cohesion - chỉ tập trung vào password operations
 */
public interface IPasswordService {
    String hashPassword(String plainPassword);
    boolean verifyPassword(String plainPassword, String hashedPassword);
}
