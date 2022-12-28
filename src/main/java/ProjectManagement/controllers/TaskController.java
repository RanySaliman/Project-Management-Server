package ProjectManagement.controllers;

import ProjectManagement.controllers.entities.TaskInput;
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
    private TaskService taskService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private NotificationsService notificationsService;


    /**
     * end point that responsible for fetching task
     *
     * @param fields - task fields
     * @return if succeed - task, else - error message
     */
    @AccessLevel(UserRole.LEADER)
    @PostMapping(value = "addTask")
    public ResponseEntity<Response<Task>> addTask(@RequestAttribute("user") User user, @RequestAttribute("board") Board board, @RequestBody TaskInput fields) {
        Response<Task> taskResponse = fromTaskInput(user.getId(), board,fields);
        if (taskResponse.isSucceed()) {
            Response<Task> task = taskService.addTask(taskResponse.getData());
            if (task.isSucceed()) {
                notificationsService.notificationHappenedOnBoard(task.getData(), task.getData().getBoard(), Events.NewTask);
                return ResponseEntity.ok(task);
            } else {
                return ResponseEntity.badRequest().body(task);
            }
        } else {
            return ResponseEntity.badRequest().body(taskResponse);
        }
    }

    private Response<Task> fromTaskInput(int userID, Board board, TaskInput taskInput) {
        Optional<LocalDateTime> optionalDueDate = ControllerUtil.convertOffsetToLocalDateTime(taskInput.getDueDate());
        Response<Void> isValidSyntax = Validation.isValidTaskInput(taskInput);
        if (!isValidSyntax.isSucceed()) {
            return Response.createFailureResponse(isValidSyntax.getMessage());
        }
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
    }

    @AccessLevel(UserRole.ADMIN)
    @DeleteMapping(value = "deleteTask/{taskId}")
    public ResponseEntity<Task> deleteTask(@RequestAttribute("user") User user, @PathVariable("taskId") int taskId) {
        Response<Task> task = taskService.getTask(taskId);
        if (task.isSucceed()) {
                taskService.deleteTask(taskId);
                notificationsService.notificationHappened(task.getData(), Events.DeleteTask);
                return ResponseEntity.ok(task.getData());
        }
        return ResponseEntity.badRequest().body(null);
    }
}
