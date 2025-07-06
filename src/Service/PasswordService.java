package Service;

import Service.Interface.IPasswordService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * High cohesion - chỉ tập trung vào mật khẩu
 * Single Responsibility Principle
 */
public class PasswordService implements IPasswordService {

    private static final String HASH_ALGORITHM = "SHA-256";

    @Override
    public String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("Password không được null");
        }

        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi hash algorithm: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        // Hỗ trợ backward compatibility với password chưa hash
        if (plainPassword.equals(hashedPassword)) {
            return true;
        }

        String hashedInput = hashPassword(plainPassword);
        return hashedInput.equals(hashedPassword);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
