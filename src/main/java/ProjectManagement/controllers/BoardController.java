package ProjectManagement.controllers;


import ProjectManagement.controllers.entities.TaskFields;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.BoardToUser;
import ProjectManagement.entities.Response;

import ProjectManagement.entities.Task;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.entities.enums.UserActions;
import ProjectManagement.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private PermissionService permissionService;
    @Autowired
    private NotificationsService notificationsService;

    /**
     * end point that responsible for fetching board
     *
     * @return board
     * @header id
     */
    @RequestMapping(value = "getBoard", method = RequestMethod.GET)
    public ResponseEntity<Board> getBoard(@RequestHeader int id) {
        Response<Board> board = boardService.getBoard(id);
        if (board.isSucceed()) {
            return ResponseEntity.ok(board.getData());
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "createBoard")
    public ResponseEntity<String> createBoard(@RequestAttribute("userId") int userId, @RequestParam("name") String boardName) {
        Response<User> user = userService.getUserById(userId);
        if (user.isSucceed()) {
            Board board = boardService.createBoard(user.getData(), boardName);
            notificationsService.notificationHappened(board, Events.NewBoard);
            return ResponseEntity.ok("Board created successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping(value = "deleteBoard/{boardId}")
    public ResponseEntity<String> deleteBoard(@RequestParam int userId,@PathVariable("boardId") int boardId) {
        Response<Void> checkPermission = permissionService.checkPermission(userId, boardId,UserActions.DeleteBoard);
        if (checkPermission.isSucceed()) {
            String tryDelete = boardService.deleteBoard(boardId).getData();
            return ResponseEntity.ok(tryDelete);
        }
        else return ResponseEntity.badRequest().body("not permitted to delete board");
    }

    @PostMapping(value = "addUserToBoard/{userId}")
    public ResponseEntity<String> addUserToBoard(@RequestParam int boardId,  @RequestParam("userRole") String userRole, @PathVariable("userId") int userId) {
        if((boardService.addUserToBoard(boardId,userId,userRole)).getClass()== BoardToUser.class){
            return ResponseEntity.ok("user added to board");
        }else return ResponseEntity.badRequest().body("user not added");
    }

    @GetMapping(value = "/filter")
    public List<Task> filter(@RequestBody TaskFields filterFields) {
        return taskService.filter(filterFields);
    }


    @GetMapping(value = "/getAllTasks")
    public List<Task> getAllTasks(@RequestParam int boardId)  {
        return taskService.getAll(boardId);
    }
}
