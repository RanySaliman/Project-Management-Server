package ProjectManagement.entities.enums;

public enum UserRole {
    ADMIN, LEADER , REGISTERED;
    public static boolean hasAccess(UserRole userRole, UserRole requiredRole) {
        if(userRole == null || requiredRole == null) return false;
        if(userRole == ADMIN) return true;
        if(userRole == LEADER && requiredRole != ADMIN) return true;
        if(userRole == REGISTERED && requiredRole == REGISTERED) return true;
        return false;
    }
}
