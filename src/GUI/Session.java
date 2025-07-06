package GUI;

import Model.User;

public class Session {
    public static User currentUser;

    // Legacy flags - được sử dụng trong các Panel và MainFrame
    public static boolean isAdminFlag;
    public static boolean isManagerFlag;

    // Enum cho roles - để mở rộng tương lai
    public enum Role {
        ADMIN("admin"),
        PRODUCT_MANAGER("product_manager"),
        CUSTOMER("customer");

        private final String roleName;

        Role(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }

        public static Role fromString(String roleName) {
            for (Role role : Role.values()) {
                if (role.roleName.equalsIgnoreCase(roleName)) {
                    return role;
                }
            }
            return CUSTOMER; // Default role
        }
    }

    // Current user role
    public static Role currentRole;

    public static void setCurrentUser(User user, String roleName) {
        currentUser = user;
        currentRole = Role.fromString(roleName);

        //Legacy flags để tương thích với code cũ
        isAdminFlag = (currentRole == Role.ADMIN);
        isManagerFlag = (currentRole == Role.PRODUCT_MANAGER);
    }
}
