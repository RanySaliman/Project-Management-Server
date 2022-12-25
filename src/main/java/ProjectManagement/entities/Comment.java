package ProjectManagement.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Embeddable
@Data
public class Comment {
    private String content;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;
}
