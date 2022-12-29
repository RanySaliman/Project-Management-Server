package ProjectManagement.controllers;


import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.entities.User;
import ProjectManagement.entities.annotations.AccessLevel;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.services.BoardService;
import ProjectManagement.services.NotificationsService;
import ProjectManagement.services.TaskService;
import ProjectManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/board")
public class BoardController {


    @Autowired
    private TaskService taskService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationsService notificationsService;

    /**
     * Gets the details of a board.
     * only users who are register to this board can access this method.
     *
     * @param board the board to be retrieved
     * @return an HTTP response with the board if the request is successful,
     * or an HTTP error response if the request fails
     */
    @AccessLevel(UserRole.REGISTERED)
    @RequestMapping(value = "getBoard", method = RequestMethod.GET)
    public ResponseEntity<Board> getBoard(@RequestAttribute("board") Board board) {
        return ResponseEntity.ok(board);
    }

    /**
     * Creates a new board.
     * every user can create a board so the method doesn't have any access level.
     *
     * @param userId    the ID of the user who is creating the board
     * @param boardName the name of the new board
     * @return an HTTP response with the new board if the request is successful,
     * or an HTTP error response with an error message if the request fails
     */
    @PostMapping(value = "createBoard")
    public ResponseEntity<Object> createBoard(@RequestAttribute("userId") int userId, @RequestParam("name") String boardName) {
        Response<User> user = userService.getUserById(userId);
        if (user.isSucceed()) {
            Response<Board> optionalBoard = boardService.createBoard(user.getData(), boardName);
            if (optionalBoard.isSucceed()) {
                notificationsService.notificationHappened(optionalBoard.getData(), Events.NewBoard);
                return ResponseEntity.ok(optionalBoard.getData());
            } else {
                return ResponseEntity.badRequest().body("Cannot create a board because " + optionalBoard.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    /**
     * Deletes a board.
     * only the admin of the board can delete the board.
     *
     * @param board the board to be deleted
     * @return an HTTP response with a success message if the request is successful,
     * or an HTTP error response if the request fails
     */
    @AccessLevel(UserRole.ADMIN)
    @PostMapping(value = "deleteBoard")
    public ResponseEntity<String> deleteBoard(@RequestAttribute("board") Board board) {
        boardService.deleteBoard(board);
        return ResponseEntity.ok("Board deleted successfully");
    }

    /**
     * Adds a user to a board.
     * only the admin of the board can add a user to the board.
     *
     * @param user          the user who is making the request
     * @param board         the board to which the user will be added
     * @param userRole      the role of the user being added to the board
     * @param userNameToAdd the name of the user to be added to the board
     * @return an HTTP response with a success message if the request is successful,
     * or an HTTP error response with an error message if the request fails
     */
    @AccessLevel(UserRole.ADMIN)
    @PostMapping(value = "addUserToBoard/{boardId}")
    public ResponseEntity<String> addUserToBoard(@RequestAttribute("user") User user, @RequestAttribute("board") Board board, @RequestParam("userRole") String userRole, @RequestParam("adduser") String userNameToAdd) {
        Response<User> optionalUser = userService.getUserByName(userNameToAdd);
        if (optionalUser.isSucceed()) {
            boardService.addUserToBoard(optionalUser.getData(), board, UserRole.valueOf(userRole));
            return ResponseEntity.ok("User added successfully");
        }
        return ResponseEntity.badRequest().body("could not add user to board");
    }

    /**
     * Filters the tasks of a board based on certain criteria.
     * only users who are register to this board can access this method.
     *
     * @param board        the board whose tasks will be filtered
     * @param filterFields the criteria based on which the tasks will be filtered
     * @return a set of tasks that match the given criteria
     */
    @AccessLevel(UserRole.REGISTERED)
    @GetMapping(value = "/filter")
    public Set<Task> filter(@RequestAttribute("board") Board board, @RequestBody TaskFields filterFields) {
        return taskService.filter(board, filterFields);
    }

    /**
     * Gets all the tasks of a board.
     * only users who are register to this board can access this method.
     *
     * @param board the board whose tasks will be retrieved
     * @return a set of tasks belonging to the given board
     */
    @AccessLevel(UserRole.REGISTERED)
    @GetMapping(value = "/getAllTasks")
    public Set<Task> getAllTasks(@RequestAttribute("board") Board board) {
        return boardService.getAllTasks(board);
    }

}
