package ProjectManagement.entities;

import javax.management.relation.Role;
import java.io.Serializable;

public class CompositeKeyBoardUser implements Serializable {
        private int userId;
        private Role userRole;
        private int boardId;
}
