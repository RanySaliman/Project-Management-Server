package ProjectManagement.controllers.entities;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Task;
import ProjectManagement.entities.UserInBoard;
import ProjectManagement.entities.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class BoardToPresent {
    private int boardId;
    private String boardName;
    private Set<Task> tasks;
    private Set<String> taskTypes;
    private Set<String> statuses;
    private Set<UserInBoardToPresent> users;

    public static BoardToPresent fromBoard(Board board) {
        BoardToPresent boardToPresent = new BoardToPresent();
        boardToPresent.boardId = board.getId();
        boardToPresent.boardName = board.getName();
        boardToPresent.tasks = board.getTasks();
        boardToPresent.taskTypes = board.getTaskTypes();
        boardToPresent.statuses = board.getStatuses();
        boardToPresent.users = board.getUsers().stream().map(userInBoard -> UserInBoardToPresent.fromUserInBoard(userInBoard)).collect(Collectors.toSet());
        return boardToPresent;
    }

    @NoArgsConstructor
    @Data
    public static class UserInBoardToPresent {
        private String userName;
        private String email;
        private UserRole role;

        public static UserInBoardToPresent fromUserInBoard(UserInBoard userInBoard) {
            UserInBoardToPresent userInBoardToPresent = new UserInBoardToPresent();
            userInBoardToPresent.userName = userInBoard.getUser().getUsername();
            userInBoardToPresent.email = userInBoard.getUser().getEmail();
            userInBoardToPresent.role = userInBoard.getUserRole();
            return userInBoardToPresent;
        }
    }
}
