package ProjectManagement.controllers;


import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.*;
import ProjectManagement.entities.annotations.AccessLevel;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.entities.enums.UserRole;
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
    private NotificationsService notificationsService;

    /**
     * end point that responsible for fetching board
     * @return board
     */
    @RequestMapping(value = "getBoard/{boardId}", method = RequestMethod.GET)
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
            Response<Board> optionalBoard = boardService.createBoard(user.getData(), boardName);
            if (optionalBoard.isSucceed()) {
                notificationsService.notificationHappened(optionalBoard.getData(), Events.NewBoard);
                return ResponseEntity.ok("Board created successfully");
            } else {
                return ResponseEntity.badRequest().body(optionalBoard.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @AccessLevel(UserRole.ADMIN)
    @PostMapping(value = "deleteBoard/{boardId}")
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
//
//    @PutMapping("/{boardId}/statusChange/{status}")
//    public ResponseEntity<Void> updateBoardStatus(@PathVariable int boardId, @PathVariable String status) {
//        // Retrieve the board with the given ID
//        Optional<Board> optionalBoard = boardRepository.findById(boardId);
//        if (!optionalBoard.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        Board board = optionalBoard.get();
//
//        // Update the board's status
//        board.getStatuses().add(status);
//        boardRepository.save(board);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    public Board(String name) {
//        this.name = name;
//        this.users=new HashSet<>();
//        this.tasks=new HashSet<>();
//        this.taskTypes=new HashSet<>(Set.of("Task","Bug","Subtask"));
//        this.statuses=new HashSet<>(Set.of("Open","In Progress","Done"));
//    }

}
