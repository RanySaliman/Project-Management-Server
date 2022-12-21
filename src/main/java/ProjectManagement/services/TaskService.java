package ProjectManagement.services;

import ProjectManagement.entities.Task;
import ProjectManagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Task> filter(int boardId, Optional<Integer> id, Optional<Integer> taskParentId, Optional<Integer> creator, Optional<Integer> assignedUserId
                     , Optional<LocalDateTime> dueDate, Optional<Integer> importance, Optional<String> title, Optional<String> description){
        if(id.isPresent()){
            return taskRepository.findAll().stream()
                    .filter(task -> task.getBoardId() == id.get()).collect(Collectors.toList());
        }
        if(taskParentId.isPresent()){
            return taskRepository.findAll().stream()
                    .filter(task -> task.getBoardId() == taskParentId.get()).collect(Collectors.toList());
        }

    }
}
