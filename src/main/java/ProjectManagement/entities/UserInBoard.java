package ProjectManagement.entities;

import ProjectManagement.entities.enums.NotificationMethod;
import ProjectManagement.entities.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "notificationMethods",   joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    @Column(name = "notificationMethods")
   Set<NotificationMethod> notificationMethods;

    public UserInBoard(User user, UserRole userRole, Set<NotificationMethod> notificationMethods) {
        this.user = user;
        this.userId = user.getId();
        this.userRole = userRole;
        this.notificationMethods=new HashSet<>(notificationMethods);
    }
}