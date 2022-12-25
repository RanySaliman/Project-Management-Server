package ProjectManagement.entities;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "task")
public class Task {

    private int boardId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int taskParentId;
    private int creator;
    private int assignedUserId;
    LocalDateTime dueDate;
    private Integer importance; // 1-5 where 5 is the highest priority
    String title;
    String description;

    public Task(int boardId, int taskParentId, int assignedUserId, int importance, String title, String description) {
        this.boardId = boardId;
        this.taskParentId = taskParentId;
        this.assignedUserId = assignedUserId;
        this.dueDate = LocalDateTime.now();
        this.importance = importance;
        this.title = title;
        this.description = description;
    }
}
