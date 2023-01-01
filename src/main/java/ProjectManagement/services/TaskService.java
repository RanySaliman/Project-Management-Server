package ProjectManagement.services;
import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Comment;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    /**
     * Filters the tasks in the given board based on the given filter fields.
     * Only tasks that match all the specified filter fields will be included in the result.
     * If a filter field is null or not specified, it will not be used as a filter criterion and all tasks will be included.
     * every field can be null or not specified (in which case it will not be used as a filter criterion).
     *
     * @param board        the board whose tasks to filter
     * @param filterFields the filter fields to apply, or null to return all tasks in the board
     * @return a set of tasks that match all the specified filter fields, or all tasks in the board if no filter fields are specified
     */
    public Set<Task> filter(Board board, TaskFields filterFields) {
        Integer creator = filterFields.getCreator();
        String title = filterFields.getTitle();
        LocalDateTime dueDate = filterFields.getDueDate();
        Integer importance =filterFields.getImportance();
        String description= filterFields.getDescription();
        Integer taskParentId= filterFields.getTaskParentId();
        String status = filterFields.getStatus();
        String type = filterFields.getType();
        Stream<Task> filteredTask = board.getTasks().stream();
        if(creator > 0){
            filteredTask =filteredTask.filter(task -> task.getCreator() == creator);


        }
        if (description != null) {
            filteredTask = filteredTask
                    .filter(task -> Objects.equals(task.getDescription(), description));
        }
        if (title != null) {
            filteredTask = filteredTask
                    .filter(task -> Objects.equals(task.getTitle(), title));
        }

        if (dueDate != null) {
            filteredTask = filteredTask
                    .filter(task -> task.getDueDate() == dueDate);
        }
        if (importance > 0) {
            filteredTask = filteredTask
                    .filter(task -> task.getImportance() == importance);
        }

        if (taskParentId > 0) {
            filteredTask = filteredTask
                    .filter(task -> task.getTaskParentId() == taskParentId);
        }

        if(status != null){
            filteredTask= filteredTask.filter(task -> Objects.equals(task.getStatus(), status));
        }

        if( type!= null){
            filteredTask= filteredTask.filter(task -> Objects.equals(task.getType(), type));
        }
        return filteredTask.collect(Collectors.toSet());
    }

    /**
     * Adds the given task to the repository.
     *
     * @param task the task to add
     * @return a response object with the saved task if the task was added successfully, or an error message otherwise
     */
    public Response<Task> addTask(Task task) {
        Task savedTask = taskRepository.save(task);
        return Response.createSuccessfulResponse(savedTask);
    }

    /**
     * Returns the task with the given ID.
     *
     * @param taskId the ID of the task to return
     * @return a response object with the task if it was found, or an error message otherwise
     */
    public Response<Task> getTask(int taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            return Response.createSuccessfulResponse(task.get());
        } else {
            return Response.createFailureResponse("task not found");
        }
    }

    public Response<Void> deleteTask(Task task) {
        Board board = task.getBoard();
        board.getTasks().remove(task);
        taskRepository.delete(task);
        Optional<Task> result2 = taskRepository.findById(task.getId());
        System.out.println(result2.isPresent());

        return Response.createSuccessfulResponse(null);
    }

    /**
     * Updates the status of the given task to the given status.
     *
     * @param task   the task to update
     * @param status the new status for the task
     */
    public void updateTaskStatus(Task task, String status) {
        task.setStatus(status);
        taskRepository.save(task);
    }

    /**
     * Updates the type of the given task to the given type.
     *
     * @param task the task to update
     * @param type the new type for the task
     */
    public void updateTaskType(Task task, String type) {
        task.setType(type);
        taskRepository.save(task);
    }

    /**
     * Adds the given comment to the given task.
     *
     * @param task    the task to add the comment to
     * @param comment the comment to add
     * @return the updated task with the added comment
     */
    public Task addComment(Task task, Comment comment) {
        task.getComments().add(comment);
        return taskRepository.save(task);
    }
}
