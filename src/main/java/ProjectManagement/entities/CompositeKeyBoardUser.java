package ProjectManagement.entities;

import javax.management.relation.Role;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CompositeKeyBoardUser implements Serializable {
        @Id
        private int userId;
        @Column(name="user_role")
        private Role userRole;
        @Id
        private int boardId;
}
