package ProjectManagement.services;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.UserActions;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.utils.PermissionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PermissionService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;


    /**
     * Checks if a given user (represented by user id) has permission to perform the given action in the system.
     *
     * @param userId - int, id of user that we want to check if he has a permission to perform the given action.
     * @param action - UserActions Enum, the action we need to check if the user can perform.
     * @return Response<Boolean> object, contains failure response - if user wasn't found. returns response with false if user doesn't have permission to perform action, and response with true if user has the permission.
     */
    public Response<Void> checkPermission(int userId, int boardId, UserActions action) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board;
        User user;
        if (!optionalUser.isPresent()) {
            return Response.createFailureResponse(String.format("User with id: %d does not exist or board doesn't exists", userId));
        }
        if (!optionalBoard.isPresent()) {
            return Response.createFailureResponse(String.format("Board with id: %d does not exist", boardId));
        }
        board = optionalBoard.get();
        user = optionalUser.get();
        return checkPermission(user, board, action);
    }

    public Response<Void> checkPermission(User user, Board board, UserActions action) {
        if(board.getUsers() == null){
            return Response.createFailureResponse("There is no users in the board");
        }
        UserRole userRole = board.getUsers().get(user);
        if (userRole == null) {
            return Response.createFailureResponse("User doesn't have permission to perform this action");
        }
        if (PermissionsManager.hasPermission(userRole, action)) {
            return Response.createSuccessfulResponse(null);
        }
        return Response.createFailureResponse("User doesn't have permission to perform this action");
    }
}