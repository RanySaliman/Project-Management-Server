package ProjectManagement.entities;

import ProjectManagement.entities.enums.NotificationMethod;
import ProjectManagement.entities.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "UserInBoard")
public class UserInBoard {
    @Id
    @Column(name = "user_id")
    int userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @Enumerated(EnumType.STRING)
    UserRole userRole;

    // add another field here
    NotificationMethod notificationMethod;

    public UserInBoard(User user, UserRole userRole, NotificationMethod notificationMethod) {
        this.user = user;
        this.userId = user.getId();
        this.userRole = userRole;
        this.notificationMethod = notificationMethod;
    }
}