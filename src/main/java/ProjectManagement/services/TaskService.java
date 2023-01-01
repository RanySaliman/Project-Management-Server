package ProjectManagement.services;


import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Comment;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private BoardRepository boardRepository;




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

    public Response<Task> addTask(Task task) {
        Task savedTask = taskRepository.save(task);
        return Response.createSuccessfulResponse(savedTask);
    }

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

    public void updateTaskStatus(Task task, String status){
        task.setStatus(status);
        taskRepository.save(task);
    }
    public void updateTaskType(Task task, String type){
        task.setType(type);
        taskRepository.save(task);
    }
    public Task addComment(Task task, Comment comment){
        task.getComments().add(comment);
        return taskRepository.save(task);
    }
}
