package ProjectManagement.entities;

import javax.persistence.Id;

public class CompositeKeyBoardStatusType {
    @Id
    int boardId;
    @Id
    int statusId;
    @Id
    int typeId;
}
