package ProjectManagement.services;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.repositories.BoardUserRepository;
import ProjectManagement.utils.PermissionsManager;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.UserActions;
import ProjectManagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PermissionService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardUserRepository boardUserRepository;



    /**
     * Checks if a given user (represented by user id) has permission to perform the given action in the system.
     * @param userId - int, id of user that we want to check if he has a permission to perform the given action.
     * @param action - UserActions Enum, the action we need to check if the user can perform.
     * @return Response<Boolean> object, contains failure response - if user wasn't found. returns response with false if user doesn't have permission to perform action, and response with true if user has the permission.
     */
    public Response<Boolean> checkPermission(int userId, int boardId, UserActions action) {
        Optional<UserRole> optionalUserRole = boardUserRepository.findByUserIdAndBoardId(userId, boardId);
        if (!optionalUserRole.isPresent()) {
            return Response.createFailureResponse(String.format("User with id: %d does not exist or board doesn't exists", userId));
        }
        UserRole userRole = optionalUserRole.get();
        if( PermissionsManager.hasPermission(userRole,action)) {
            return Response.createSuccessfulResponse(true);

        }
        return Response.createSuccessfulResponse(false);
    }
}