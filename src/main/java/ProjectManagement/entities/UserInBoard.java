package ProjectManagement.entities;

import ProjectManagement.entities.enums.NotificationMethod;
import ProjectManagement.entities.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@Embeddable
public class UserInBoard {
    @Id
    User user;
    UserRole role;
    NotificationMethod notificationMethod;
    public UserInBoard(User user, UserRole role, NotificationMethod notificationMethod) {
        this.user = user;
        this.role = role;
        this.notificationMethod = notificationMethod;
    }

}
