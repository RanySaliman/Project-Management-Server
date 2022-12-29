package ProjectManagement.services;

import ProjectManagement.entities.*;
import ProjectManagement.entities.enums.NotificationMethod;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;


    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Response<Board> addUserToBoard(int boardId, int userId, UserRole userRole) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Board> board = boardRepository.findById(boardId);
        if (!user.isPresent() || !board.isPresent()) {
            return Response.createFailureResponse(String.format("User with id: %d does not exist", userId));
        }
        return Response.createSuccessfulResponse(addUserToBoard(user.get(), board.get(), userRole));
    }

    public Board addUserToBoard(User user, Board board, UserRole userRole) {
        UserInBoard userInBoard = new UserInBoard();
        userInBoard.setId(new UserInBoardId(board.getId(), user.getId()));
        userInBoard.setUser(user);
        userInBoard.setUserRole(userRole);
        userInBoard.setNotificationMethods(Set.of(NotificationMethod.EMAIL, NotificationMethod.POPUP));
        board.getUsers().add(userInBoard);
        return boardRepository.save(board);
    }

    /**
     * method that responsible for fetching board for specific board id
     *
     * @param id
     * @return board
     */
    public Response<Board> getBoard(int id) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return Response.createFailureResponse("");
        } else {
            return Response.createSuccessfulResponse(board.get());
        }
    }

    public Response<Board> createBoard(User Creator, String boardName) {
        Board board = new Board(boardName);
        boardRepository.save(board);
        return Response.createSuccessfulResponse(addUserToBoard(Creator, board, UserRole.ADMIN));
    }

    public Response<String> deleteBoard(Board board) {
        boardRepository.delete(board);
        return Response.createSuccessfulResponse("Board successfully deleted");
    }

    public Response<String> addUserToBoard(int boardId, UserInBoard userInBoard) {
        Optional<Board> board = boardRepository.getBoardById(boardId);
        if (!board.isPresent()) {
            return Response.createFailureResponse("Board not found");
        }
        board.get().getUsers().add(userInBoard);
        boardRepository.save(board.get());
        return Response.createSuccessfulResponse("User successfully added to board");
    }

    public Set<Task> getAllTasks(Board board) {
        return board.getTasks();
    }

    public void updateBoardStatus(Board board, String status) {
        if (board.getStatuses().add(status)) {
            boardRepository.save(board);
        }
    }

    public void updateBoardType(Board board, String taskType) {
        if (board.getTaskTypes().add(taskType)) {
            boardRepository.save(board);
        }
    }
}
