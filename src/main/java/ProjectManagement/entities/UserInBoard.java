package ProjectManagement.entities;

import ProjectManagement.entities.enums.NotificationMethod;
import ProjectManagement.entities.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "UserInBoard")
public class UserInBoard {
    @EmbeddedId
    UserInBoardId id;

    @ManyToOne
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    Board board;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @Enumerated(EnumType.STRING)
    UserRole userRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "NotificationMethods", joinColumns = {@JoinColumn(name = "user_id"),@JoinColumn(name = "board_id")})
    @Column(name = "notification_method")
    Set<NotificationMethod> NotificationMethods;
}