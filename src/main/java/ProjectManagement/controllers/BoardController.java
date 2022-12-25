package ProjectManagement.controllers;


import ProjectManagement.controllers.entities.FilterFields;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;

import ProjectManagement.entities.Task;
import ProjectManagement.entities.enums.UserActions;
import ProjectManagement.services.AuthService;
import ProjectManagement.services.BoardService;
import ProjectManagement.services.PermissionService;
import ProjectManagement.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/board")
public class BoardController {


    @Autowired
    private AuthService authService;
    @Autowired
    private TaskService taskService;


    @Autowired
    private BoardService boardService;
   // @Autowired
    //private PermissionService permissionService;
    /**
     * end point that responsible for fetching board
     * @header id
     * @return board
     */
    @RequestMapping(value = "getBoard", method = RequestMethod.GET)
    public ResponseEntity<Board> getBoard(@RequestHeader int id) {
        Response<Board> board = boardService.getBoard(id);
        if(board.isSucceed()){
            return ResponseEntity.ok(board.getData());
        }else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "createBoard")
    public ResponseEntity<String> createBoard(@RequestBody Board board) {
        boardService.createBoard(board.getName());
        return ResponseEntity.ok("Board created successfully");
    }
/*
    @PostMapping(value = "deleteBoard/{boardId}")
    public ResponseEntity<String> deleteBoard(@RequestParam int userId,@PathVariable("boardId") int boardId) {
        Response<Boolean> checkPermission = permissionService.checkPermission(userId, boardId, UserActions.DeleteBoard);
        if (checkPermission.isSucceed()) {
            if (checkPermission.getData()) {
                return ResponseEntity.ok(boardService.deleteBoard(boardId).getData());
            }else return ResponseEntity.badRequest().body("not permitted to delete board");
        }else return ResponseEntity.badRequest().body("wrong id");
    }
*/

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public List<Task> filter(@RequestBody FilterFields filterFields) {
        return taskService.filter(filterFields.getBoardId(),filterFields);
    }


    @RequestMapping(value = "/getAllTasks", method = RequestMethod.GET)
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }
}
