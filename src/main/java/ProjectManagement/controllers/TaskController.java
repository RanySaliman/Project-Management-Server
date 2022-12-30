package ProjectManagement.controllers;

import ProjectManagement.controllers.entities.TaskInput;
import ProjectManagement.entities.*;
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
     * Adds a task to a board.
     * every user who is leader or admin of the board can add a task to the board.
     *
     * @param user   the user who is adding the task
     * @param board  the board to which the task will be added
     * @param fields the input fields for the task
     * @return an HTTP response with a response object containing the added task if the request is successful,
     * or an HTTP error response with a response object containing an error message if the request fails
     */
    @AccessLevel(UserRole.LEADER)
    @PostMapping(value = "addTask")
    public ResponseEntity<Response<Task>> addTask(@RequestAttribute("user") User user, @RequestAttribute("board") Board board, @RequestBody TaskInput fields) {
        Response<Task> taskResponse = fromTaskInput(user.getId(), board, fields);
        if (taskResponse.isSucceed()) {
            Response<Task> task = taskService.addTask(taskResponse.getData());
            if (task.isSucceed()) {
                notificationsService.notificationHappenedOnBoard(task.getData(), task.getData().getBoard(), Events.NewTask);
                notificationsService.popNotificationHappenedOnBoard(task.getData(), task.getData().getBoard(), Events.NewTask);
                return ResponseEntity.ok(task);
            } else {
                return ResponseEntity.badRequest().body(task);
            }
        } else {
            return ResponseEntity.badRequest().body(taskResponse);
        }
    }

    /**
     * Takes input from the user as taskInput and convert it to a new task object.
     *
     * @param userID    the ID of the user creating the task
     * @param board     the board to which the task belongs
     * @param taskInput the task input object to be converted
     * @return a response object containing the task if the input is valid, or an error message if the input is invalid
     */
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

    /**
     * Deletes a task from a board.
     * just admin can delete a task.
     *
     * @param user   the user who is deleting the task
     * @param taskId the ID of the task to be deleted
     * @return an HTTP response with the deleted task if the request is successful,
     * or an HTTP error response with a null body if the request fails
     */
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

    /**
     * Updates the status of a task.
     * every user who is registered to the board can update the status of a task.
     *
     * @param board  the board to which the task belongs
     * @param status the new status of the task
     * @param taskId the ID of the task to be updated
     * @return an HTTP response with no body if the request is successful,
     * or an HTTP error response with a null body if the request fails
     */
    @AccessLevel(UserRole.REGISTERED)
    @PutMapping("/statusUpdate/{task}/{status}")
    public ResponseEntity<Void> updateTaskStatus(@RequestAttribute("board") Board board, @PathVariable("status") String status, @PathVariable("task") int taskId) {

        Response<Task> task = taskService.getTask(taskId);
        if (task.isSucceed()) {
            // Update the board's status
            boardService.updateBoardStatus(board, status);
            taskService.updateTaskStatus(task.getData(), status);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.badRequest().body(null);
    }

    /**
     * Updates the type of  task.
     * every user who is registered to the board can update the type of a task.
     *
     * @param board  the board to which the task belongs
     * @param type   the new type of the task
     * @param taskId the ID of the task to be updated
     * @return an HTTP response with no content if the request is successful,
     * or an HTTP error response with a null body if the request fails
     */
    @AccessLevel(UserRole.REGISTERED)
    @PutMapping("/typeUpdate/{task}/{type}")
    public ResponseEntity<Void> updateTaskType(@RequestAttribute("board") Board board, @PathVariable("type") String type, @PathVariable("task") int taskId) {

        Response<Task> task = taskService.getTask(taskId);
        if (task.isSucceed()) {
            // Update the board's status
            boardService.updateBoardType(board, type);
            taskService.updateTaskType(task.getData(), type);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.badRequest().body(null);
    }

    /**
     * Adds a comment to a task.
     * only users who are registered to board can add a comment to a task.
     *
     * @param user    the user who is adding the comment
     * @param content the content of the comment
     * @param taskId  the ID of the task to which the comment will be added
     * @return an HTTP response with a response object containing the updated task if the request is successful,
     * or an HTTP error response with a response object containing an error message if the request fails
     */
    @AccessLevel(UserRole.REGISTERED)
    @PostMapping(value = "addComment/{taskId}")
    public ResponseEntity<Response<Task>> addComment(@RequestAttribute("user") User user, @RequestBody String content, @PathVariable int taskId) {
        Comment comment = new Comment(content, user);
        Response<Task> task = taskService.getTask(taskId);
        if (task.isSucceed()) {
            taskService.addComment(task.getData(), comment);
            return ResponseEntity.ok(Response.createSuccessfulResponse(taskService.addComment(task.getData(), comment)));
        } else return ResponseEntity.badRequest().body(Response.createFailureResponse("task not found"));
    }
}
