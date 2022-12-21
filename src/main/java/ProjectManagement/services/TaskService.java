package ProjectManagement.services;

<<<<<<< HEAD
import ProjectManagement.controllers.entities.FilterFields;
=======
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
>>>>>>> main
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import java.time.LocalDateTime;
=======

>>>>>>> main
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    private FilterFields fields;
    private FilterFields filterFields;


    public List<Task> filter(int boardId, FilterFields filterFields){
        Integer creator = filterFields.getCreator();
        String title = filterFields.getTitle();
        LocalDateTime dueDate = filterFields.getDueDate();

        if(creator != null){
            return taskRepository.findAll().stream()
                    .filter(task -> task.getBoardId() == creator).collect(Collectors.toList());
        }
        if(title != null){
            return taskRepository.findAll().stream()
                    .filter(task -> task.getTitle() == title).collect(Collectors.toList());
        }

        if(dueDate != null){
            return taskRepository.findAll().stream()
                    .filter(task -> task.getDueDate() == dueDate).collect(Collectors.toList());
        }

        return null;
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
