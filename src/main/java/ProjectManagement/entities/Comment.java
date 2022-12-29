package ProjectManagement.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
public class Comment {
    private String content;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    public Comment(String content, User creator) {
        this.content = content;
        this.time = LocalDateTime.now();
        this.creator = creator;
    }


}
