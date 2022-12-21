package ProjectManagement.services;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    /*
    private int boardId;
    @Id
    private int id;
    private int taskParentId;
    private int creator = dani;
    private int assignedUserId ;
    LocalDateTime DueDate;
    int importance; // 1-5 where 5 is the highest priority
    String title;
    String description;*/

    public List<Task> filter(int boardId, Optional<Integer> importance){

        return taskRepository.findAll().stream()
        .filter(task -> task.getBoardId() == boardId).
                collect(Collectors.toList());
    }

    public Response<Task> addTask(int boardId, int taskParentId, int assignedUserId, int importance, String title, String description){
        return Response.createSuccessfulResponse(taskRepository.save(new Task(boardId, taskParentId, assignedUserId, importance, title, description)));
    }

        /*
            private int boardId;
    @Id
    private int id;
    private int taskParentId;
    private int creator;
    private int assignedUserId;
    LocalDateTime DueDate;
    int importance; // 1-5 where 5 is the highest priority
    String title;
    String description;


         */
}
