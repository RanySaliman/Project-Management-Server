package ProjectManagement.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Data
public class UserInBoardId implements Serializable {
    @Column(name = "board_id")
    int boardId;
    @Column(name = "user_id")
    int userId;

    public UserInBoardId(int boardId, int userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
}
