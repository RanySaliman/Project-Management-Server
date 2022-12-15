package ProjectManagement.entities;

import ProjectManagement.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "BoardToUser")
@Getter
@Setter
@IdClass(CompositeKeyBoardUser.class)
public class BoardToUser {
    @Id
    int boardId;
    @Id
    int userId;
    private UserRole userRole;
    public BoardToUser(UserRole userRole)
    {
        this.userRole=userRole;
    }
    public BoardToUser(int boardId, int userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
    public BoardToUser() {
    }
}
