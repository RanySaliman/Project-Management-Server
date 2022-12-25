package ProjectManagement.entities;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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
    @ManyToOne
    private Board board;

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
    String status;
    String type;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "Comments", joinColumns = @JoinColumn(name = "TaskId",referencedColumnName="id", nullable = false), uniqueConstraints = @UniqueConstraint(columnNames = {"TaskId"}))
    @Column(name = "Comment")
    private Set<Comment> comments;

    public Task(Board board,  int taskParentId, int assignedUserId, int importance, String title, String description) {
        this.board=board;
        this.taskParentId = taskParentId;
        this.assignedUserId = assignedUserId;
        this.dueDate = LocalDateTime.now();
        this.importance = importance;
        this.title = title;
        this.description = description;
    }
}
