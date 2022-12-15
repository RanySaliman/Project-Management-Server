package ProjectManagement.entities;

import lombok.*;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "Task")
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
    private int id;
    private int taskParentId;
    private int creator;
    private int assignedUserId;
    LocalDateTime DueDate;
    int importance; // 1-5 where 5 is the highest priority
    String title;
    String description;


}
