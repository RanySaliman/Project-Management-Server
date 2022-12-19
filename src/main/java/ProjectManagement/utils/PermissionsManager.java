package ProjectManagement.utils;
import ProjectManagement.entities.enums.UserActions;
import ProjectManagement.entities.enums.UserRole;
import java.util.Map;
import java.util.Set;


public class PermissionsManager {

    private static final Map<UserRole, Set<UserActions>> permissions= Map.of(
            UserRole.ADMIN, Set.of(UserActions.values()),
            UserRole.LEADER,Set.of(),
            UserRole.REGISTERED, Set.of()
            );

    /**
     * Checks if given user type has permissions to perform given action.
     * @param userRole: represent the user type
     * @param userAction : represent the function the user wants to perform
     * @return true-if user authorised to do the action, otherwise-false
     */
    public static boolean hasPermission(UserRole userRole, UserActions userAction)
    {
        return permissions.get(userRole).contains(userAction);
    }

    /**
     *
     * @param userRole - to user role to whom to assign a permission to perform the given action.
     * @param userAction - the action the given user role needs to be allowed to perform.
     * Gives the User Type a new permission to perform the specified action in the system.
     */
    public void addPermission(UserRole userRole, UserActions userAction){
        permissions.get(userRole).add(userAction);

    }


    /**
     *
     * @param userRole - to user role to whom to remove a permission to perform the given action.
     * @param userAction - the action the given user role need to remove.
     * Removes the User Type's existing permission to perform the specified action in the system.
     */
    public void removePermission(UserRole userRole, UserActions userAction)
    {
        permissions.get(userRole).remove(userAction);
    }
}
