package ProjectManagement.services;


import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private BoardRepository boardRepository;

    public List<Task> filter(TaskFields filterFields){
        Integer creator = filterFields.getCreator();
        String title = filterFields.getTitle();
        LocalDateTime dueDate = filterFields.getDueDate();
        Integer importance =filterFields.getImportance();
        String description= filterFields.getDescription();
        Integer taskParentId= filterFields.getTaskParentId();
        List<Task>  filteredTak =taskRepository.findAll().stream().collect(Collectors.toList());
        if(creator > 0){
            filteredTak =filteredTak.stream().filter(task -> task.getCreator() == creator).collect(Collectors.toList());
        }
        if(description != null){
            filteredTak =filteredTak.stream()
                    .filter(task -> Objects.equals(task.getDescription(), description)).collect(Collectors.toList());
        }
        if(title != null){
            filteredTak =filteredTak.stream()
                    .filter(task -> Objects.equals(task.getTitle(), title)).collect(Collectors.toList());
        }

        if(dueDate != null){
            filteredTak = filteredTak.stream()
                    .filter(task -> task.getDueDate() == dueDate).collect(Collectors.toList());
        }
        if(importance > 0){
            filteredTak = filteredTak.stream()
                    .filter(task -> task.getImportance() == importance).collect(Collectors.toList());
        }

        if(taskParentId > 0){
            filteredTak = filteredTak.stream()
                    .filter(task -> task.getTaskParentId() == taskParentId).collect(Collectors.toList());
        }
        return filteredTak;
    }

    public Response<Task> addTask(int boardId, int taskParentId, int assignedUserId, int importance, String title, String description){
        Optional<Board> boardFromDB = boardRepository.findById(boardId);
        if(boardFromDB.isPresent()) {
            Board board = boardFromDB.get();
            Task task = new Task(board, taskParentId, assignedUserId, importance, title, description);
            Task savedTask = taskRepository.save(task);
            return Response.createSuccessfulResponse(savedTask);
        }
        return Response.createFailureResponse("Board not found");
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }


}
