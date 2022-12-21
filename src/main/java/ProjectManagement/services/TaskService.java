package ProjectManagement.services;

import ProjectManagement.controllers.entities.FilterFields;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
}
