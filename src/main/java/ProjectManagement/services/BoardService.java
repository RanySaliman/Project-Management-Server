package ProjectManagement.services;

import ProjectManagement.entities.*;
import ProjectManagement.entities.enums.NotificationMethod;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;


    /**
     * Adds a user to a board with a given user role.
     *
     * @param user     the user to be added to the board
     * @param board    the board to add the user to
     * @param userRole the role of the user in the board
     * @return the updated board
     */
    public Board addUserToBoard(User user, Board board, UserRole userRole) {
        // check if the user is already in the board and if so, update the role
        Optional<UserInBoard> isUserAlreadyInBoard = board.getUsers().stream().filter(u -> u.getUser().getId() == user.getId()).findFirst();
        if (isUserAlreadyInBoard.isPresent()) {
            if (isUserAlreadyInBoard.get().getUserRole() != userRole) {
                isUserAlreadyInBoard.get().setUserRole(userRole);
                return boardRepository.save(board);
            }
            return board;
        }
        UserInBoard userInBoard = new UserInBoard();
        userInBoard.setId(new UserInBoardId(board.getId(), user.getId()));
        userInBoard.setUser(user);
        userInBoard.setUserRole(userRole);
        userInBoard.setNotificationMethods(Set.of(NotificationMethod.EMAIL, NotificationMethod.POPUP));
        board.getUsers().add(userInBoard);
        return boardRepository.save(board);
    }

    /**
     * Fetches a board with a specific ID.
     *
     * @param id the ID of the board to fetch
     * @return a response object containing the board, or an error message if the board was not found
     */
    public Response<Board> getBoard(int id) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return Response.createFailureResponse("board not found");
        } else {
            return Response.createSuccessfulResponse(board.get());
        }
    }

    /**
     * Creates a new board with a given name and the user who created it as the board's admin.
     *
     * @param Creator   the user who is creating the board
     * @param boardName the name of the board
     * @return a response object containing the newly created board
     */
    public Response<Board> createBoard(User Creator, String boardName) {
        Board board = new Board(boardName);
        boardRepository.save(board);
        return Response.createSuccessfulResponse(addUserToBoard(Creator, board, UserRole.ADMIN));
    }

    /**
     * Deletes the given board from the repository.
     *
     * @param board the board to delete
     * @return a response object with a success message if the board was deleted successfully, or an error message otherwise
     */
    public Response<String> deleteBoard(Board board) {
        boardRepository.delete(board);
        return Response.createSuccessfulResponse("Board successfully deleted");
    }

    /**
     * Returns all tasks associated with the given board.
     *
     * @param board the board whose tasks to retrieve
     * @return a set of tasks associated with the board
     */

    public Set<Task> getAllTasks(Board board) {
        return board.getTasks();
    }

    /**
     * Updates the status of the given board to the given status.
     * If the status is not already present in the board's statuses, it will be added.
     *
     * @param board  the board to update
     * @param status the new status for the board
     */
    public void updateBoardStatus(Board board, String status) {
        if (board.getStatuses().add(status)) {
            boardRepository.save(board);
        }
    }

    /**
     * Updates the type of tasks associated with the given board to the given type.
     * If the type is not already present in the board's task types, it will be added.
     *
     * @param board    the board to update
     * @param taskType the new type of tasks for the board
     */
    public void updateBoardType(Board board, String taskType) {
        if (board.getTaskTypes().add(taskType)) {
            boardRepository.save(board);
        }
    }

    /**
     * Returns a list of all boards that the user with the given ID is a member of.
     *
     * @param userid the ID of the user
     * @return a list of boards that the user is a member of
     */
    public List<Board> getAllBoards(int userid) {
        return boardRepository.findAll().stream().filter(board -> board.getUsers().stream().anyMatch(userInBoard -> userInBoard.getId().getUserId() == userid)).collect(Collectors.toList());
    }
}
