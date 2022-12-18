package ProjectManagement.entities;

import ProjectManagement.entities.enums.UserRole;

import java.util.Map;
import java.util.Set;

public class PermissionsManager {
    private static final Map<UserRole, Set<UserActions>> permissions= Map.of(
            UserRole.ADMIN, Set.of(),
            UserRole.LEADER,Set.of(),
            UserRole.REGISTERED, Set.of()
            );
}
