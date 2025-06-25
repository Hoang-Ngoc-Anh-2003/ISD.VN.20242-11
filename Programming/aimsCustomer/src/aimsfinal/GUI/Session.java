package aimsfinal.GUI;

import aimsfinal.Model.User;
import java.util.List;

public class Session {
    private static User currentUser;
    private static boolean isManager = false;
    private static boolean isCustomer = false;
    private static String currentRole = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        if (user != null) {
            // Sử dụng role được chọn từ LoginDialog
            String chosenRole = user.getRole();
            System.out.println("DEBUG: User role from LoginDialog: " + chosenRole);
            
            if (chosenRole != null) {
                currentRole = chosenRole;
                isManager = "manager".equals(chosenRole);
                isCustomer = "customer".equals(chosenRole);
                System.out.println("DEBUG: Set role to: " + chosenRole + ", isManager: " + isManager + ", isCustomer: " + isCustomer);
            } else {
                // Fallback: kiểm tra roles list
                List<String> roles = user.getRoles();
                System.out.println("DEBUG: User roles list: " + roles);
                isManager = roles.contains("manager");
                isCustomer = roles.contains("customer");
                currentRole = isManager ? "manager" : (isCustomer ? "customer" : "customer");
                System.out.println("DEBUG: Fallback - isManager: " + isManager + ", isCustomer: " + isCustomer + ", currentRole: " + currentRole);
            }
        } else {
            clear();
        }
    }

    public static boolean isManager() {
        return isManager;
    }

    public static boolean isCustomer() {
        return isCustomer;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        System.out.println("DEBUG: Logging out user: " + (currentUser != null ? currentUser.getUsername() : "null"));
        clear();
        System.out.println("DEBUG: Logout completed - currentUser: " + currentUser + ", isManager: " + isManager + ", isCustomer: " + isCustomer);
    }

    public static void clear() {
        currentUser = null;
        isManager = false;
        isCustomer = false;
        currentRole = null;
        System.out.println("DEBUG: Session cleared - currentUser: " + currentUser + ", isManager: " + isManager + ", isCustomer: " + isCustomer);
    }
}
