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
    /*
    ● Items have the following data:
○ Board id
○ Status (open/in progress/etc..)(per board)
○ Type (Task/Bug/Subtask/etc..)(per board)
○ Parent item
○ Creator (user)
○ Assigned to (user)
○ Due date
○ Importance (1-5)
○ Title
○ Description
○ Comments
     */
    private int boardId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int taskParentId;
    private int creator;
    private int assignedUserId;
    LocalDateTime dueDate;
    int importance; // 1-5 where 5 is the highest priority
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
