package ProjectManagement.controllers;


import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.*;
import ProjectManagement.entities.annotations.AccessLevel;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.entities.enums.UserActions;
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
    @PostMapping(value = "deleteBoard/{boardId}")
    public ResponseEntity<String> deleteBoard(@RequestParam int userId, @PathVariable("boardId") int boardId) {
        Response<Void> checkPermission = permissionService.checkPermission(userId, boardId, UserActions.DeleteBoard);
        if (checkPermission.isSucceed()) {
            String tryDelete = boardService.deleteBoard(boardId).getData();
            return ResponseEntity.ok(tryDelete);
        } else return ResponseEntity.badRequest().body("not permitted to delete board");
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

//    @PostMapping(value = "addUserToBoard/{userId}")
//    public ResponseEntity<String> addUserToBoard(@RequestParam int boardId,  @RequestParam("userRole") String userRole, @PathVariable("userId") int userId) {
//        if((boardService.addUserToBoard(boardId,userId,userRole)).getClass()== BoardToUser.class){
//            return ResponseEntity.ok("user added to board");
//        }else return ResponseEntity.badRequest().body("user not added");
//    }

    @GetMapping(value = "/filter")
    public List<Task> filter(@RequestBody TaskFields filterFields) {
        return taskService.filter(filterFields);
    }


    @GetMapping(value = "/getAllTasks")
    public List<Task> getAllTasks(@RequestParam int boardId) {
        return taskService.getAll(boardId);
    }

//    @GetMapping(value = "/statusChange")
//    public Set<String> changeStatus(@RequestParam int taskId, @RequestBody String status){
//
//    }


    // Retrieve the board with the given ID


}


//    public Board(String name) {
//        this.name = name;
//        this.users=new HashSet<>();
//        this.tasks=new HashSet<>();
//        this.taskTypes=new HashSet<>(Set.of("Task","Bug","Subtask"));
//        this.statuses=new HashSet<>(Set.of("Open","In Progress","Done"));
//    }

}
