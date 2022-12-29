package ProjectManagement.controllers;
import ProjectManagement.controllers.entities.TaskInput;
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Comment;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.entities.enums.UserActions;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.services.BoardService;
import ProjectManagement.services.NotificationsService;
import ProjectManagement.services.PermissionService;
import ProjectManagement.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private BoardRepository boardRepository;


    /**
     * end point that responsible for adding a task
     *
     * @param fields - task fields
     * @return if succeed - task added, else - error message
     */
    @PostMapping(value = "addTask")
    public ResponseEntity<Response<Task>> addTask(@RequestAttribute("userId") int userId, @RequestBody TaskInput fields) {
        Response<Task> taskResponse = fromTaskInput(userId, fields);
        if (taskResponse.isSucceed()) {
            Response<Task> task = taskService.addTask(taskResponse.getData());
            if (task.isSucceed()) {
                notificationsService.notificationHappenedOnBoard(task.getData(),task.getData().getBoard(), Events.NewTask);
                notificationsService.popNotificationHappenedOnBoard(task.getData(),task.getData().getBoard(), Events.NewTask);
                return ResponseEntity.ok(task);
            } else {
                return ResponseEntity.badRequest().body(task);
            }
        } else {
            return ResponseEntity.badRequest().body(taskResponse);
        }
    }

    private Response<Task> fromTaskInput(int userID, TaskInput taskInput) {
        Response<Board> getBoard = boardService.getBoard(taskInput.getBoardId());
        Optional<LocalDateTime> optionalDueDate = ControllerUtil.convertOffsetToLocalDateTime(taskInput.getDueDate());
        Response<Void> hasPermission = permissionService.checkPermission(userID, taskInput.getBoardId(), UserActions.AddTask);
        Response<Void> isValidSyntax = Validation.isValidTaskInput(taskInput);
        if (!isValidSyntax.isSucceed()) {
            return Response.createFailureResponse(isValidSyntax.getMessage());
        }
        if (hasPermission.isSucceed() && getBoard.isSucceed()) {
            Board board = getBoard.getData();
            Task task = new Task();
            task.setBoard(board);
            task.setTaskParentId(taskInput.getTaskParentId());
            task.setImportance(taskInput.getImportance());
            task.setTitle(taskInput.getTitle());
            task.setCreator(userID);
            task.setDescription(taskInput.getDescription());
            optionalDueDate.ifPresent(task::setDueDate);
            if (board.getStatuses().contains(taskInput.getStatus())) {
                task.setStatus(taskInput.getStatus());
            } else return Response.createFailureResponse("Status not found");
            if (board.getTaskTypes().contains(taskInput.getType())) {
                task.setType(taskInput.getType());
            } else return Response.createFailureResponse("Type not found");
            return Response.createSuccessfulResponse(task);
        } else {
            if (!hasPermission.isSucceed()) {
                return Response.createFailureResponse(hasPermission.getMessage());
            } else {
                return Response.createFailureResponse("Board not found");
            }
        }
    }

    @DeleteMapping(value = "deleteTask/{taskId}")
    public ResponseEntity<Task> deleteTask(@RequestAttribute("userId") int userId, @PathVariable("taskId") int taskId) {
        Response<Task> task = taskService.getTask(taskId);
        if (task.isSucceed()) {
            Response<Void> hasPermission = permissionService.checkPermission(userId, task.getData().getBoard().getId(), UserActions.DeleteTask);
            if (hasPermission.isSucceed()) {
                taskService.deleteTask(taskId);
                notificationsService.notificationHappened(task.getData(), Events.DeleteTask);
                return ResponseEntity.ok(task.getData());
            }
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/{boardId}/statusUpdate/{task}/{status}")
    public ResponseEntity<Void> updateBoardStatus(@PathVariable int boardId, @PathVariable("status") String status, @PathVariable("task") int taskId, @RequestAttribute("userId") int userId) {
        Response<Void> checkPermission = permissionService.checkPermission(userId, boardId, UserActions.ChangeStatus);
        if (checkPermission.isSucceed()) {
            Response<Board> optionalBoard = boardService.getBoard(boardId);
            if (!optionalBoard.isSucceed()) {
                return ResponseEntity.notFound().build();
            }

            Board board = optionalBoard.getData();
            Response<Task> task = taskService.getTask(taskId);
            // Update the board's status
            board.getStatuses().add(status);
            task.getData().setStatus(status);
            boardRepository.save(board);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/{boardId}/typeUpdate/{task}/{type}")
    public ResponseEntity<Void> updateBoardType(@PathVariable int boardId, @PathVariable("type") String type, @PathVariable("task") int taskId, @RequestAttribute("userId") int userId) {
        Response<Void> checkPermission = permissionService.checkPermission(userId, boardId, UserActions.ChangeStatus);
        if (checkPermission.isSucceed()) {
            Response<Board> optionalBoard = boardService.getBoard(boardId);
            if (!optionalBoard.isSucceed()) {
                return ResponseEntity.notFound().build();
            }
            Board board = optionalBoard.getData();
            Response<Task> task = taskService.getTask(taskId);
            // Update the board's type
            board.getTaskTypes().add(type);
            task.getData().setType(type);
            boardRepository.save(board);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.badRequest().body(null);
    }

    @PostMapping(value = "addComment")
    public ResponseEntity<Response<Task>> addComment(@RequestAttribute("userId") int userId, @RequestBody String comment) {
        Response<Comment> commentResponse = Comment();
        if (commentResponse.isSucceed()) {
            Response<Task> task = taskService.addTask(taskResponse.getData());
            if (task.isSucceed()) {
                notificationsService.notificationHappenedOnBoard(task.getData(),task.getData().getBoard(), Events.NewTask);
                notificationsService.popNotificationHappenedOnBoard(task.getData(),task.getData().getBoard(), Events.NewTask);
                return ResponseEntity.ok(task);
            } else {
                return ResponseEntity.badRequest().body(task);
            }
        } else {
            return ResponseEntity.badRequest().body(taskResponse);
        }
    }
}
