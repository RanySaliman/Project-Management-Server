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
        Set<Task> filteredTak = board.getTasks();
        System.out.println((filteredTak.size()));
        if(creator > 0){
            filteredTak =filteredTak.stream().filter(task -> task.getCreator() == creator).collect(Collectors.toSet());

        }
        if (description != null) {
            filteredTak = filteredTak.stream()
                    .filter(task -> Objects.equals(task.getDescription(), description)).collect(Collectors.toSet());
        }
        if (title != null) {
            filteredTak = filteredTak.stream()
                    .filter(task -> Objects.equals(task.getTitle(), title)).collect(Collectors.toSet());
        }

        if (dueDate != null) {
            filteredTak = filteredTak.stream()
                    .filter(task -> task.getDueDate() == dueDate).collect(Collectors.toSet());
        }
        if (importance > 0) {
            filteredTak = filteredTak.stream()
                    .filter(task -> task.getImportance() == importance).collect(Collectors.toSet());
        }

        if (taskParentId > 0) {
            filteredTak = filteredTak.stream()
                    .filter(task -> task.getTaskParentId() == taskParentId).collect(Collectors.toSet());
        }
        return filteredTak;
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

    public Response<Void> deleteTask(int taskId) {
        taskRepository.deleteById(taskId);
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
