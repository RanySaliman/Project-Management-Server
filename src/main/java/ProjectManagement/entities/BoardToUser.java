package ProjectManagement.entities;

import ProjectManagement.entities.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    public BoardToUser(UserRole userRole)
    {
        this.userRole=userRole;
    }
    public BoardToUser(int boardId, int userId, String userRole) {
        this.boardId = boardId;
        this.userId = userId;
        switch (userRole){
            case "ADMIN":
                this.userRole = UserRole.ADMIN;
                break;
            case "LEADER":
                this.userRole = UserRole.LEADER;
                break;
            case "REGISTERED":
                this.userRole = UserRole.REGISTERED;
                break;
        }
    }
    public BoardToUser() {
    }


}
