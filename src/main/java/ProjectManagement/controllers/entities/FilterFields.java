package ProjectManagement.controllers.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class FilterFields {
    int boardId;
    private int id;
    private int taskParentId;
    private int creator;
    private int assignedUserId;
    LocalDateTime DueDate;
    private int importance;
    private String description;
    String title;

}
