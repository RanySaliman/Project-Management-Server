package ProjectManagement.controllers.entities;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TaskInput {
    private int boardId;
    private int taskParentId;
    private String dueDate;
    private Integer importance; // 1-5 where 5 is the highest priority
    private String title;
    private String description;
    private String status;
    private String type;
}
