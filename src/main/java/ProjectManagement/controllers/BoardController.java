package ProjectManagement.controllers;


import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.*;
import ProjectManagement.entities.annotations.AccessLevel;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.entities.enums.UserActions;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private BoardRepository boardRepository;
    /**
     * end point that responsible for fetching board
     *
     * @return board
     * @header id
     */
    @AccessLevel(UserRole.REGISTERED)
    @RequestMapping(value = "getBoard", method = RequestMethod.GET)
    public ResponseEntity<Board> getBoard(@RequestAttribute("board")Board board) {
            return ResponseEntity.ok(board);
    }

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

    @AccessLevel(UserRole.ADMIN)
    @PostMapping(value = "deleteBoard")
    public ResponseEntity<String> deleteBoard( @RequestAttribute("board") Board board) {
        boardService.deleteBoard(board);
        return ResponseEntity.ok("Board deleted successfully");
    }

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
    @AccessLevel(UserRole.REGISTERED)
    @GetMapping(value = "/filter")
    public Set<Task> filter(@RequestAttribute("board") Board board, @RequestBody TaskFields filterFields) {
        return taskService.filter(board, filterFields);
    }

    /**
     * end point that responsible for fetching all boards
     * @param board
     * @return
     */

    @AccessLevel(UserRole.REGISTERED)
    @GetMapping(value = "/getAllTasks")
    public Set<Task> getAllTasks(@RequestAttribute("board") Board board) {
        return boardService.getAllTasks(board);
    }

}
