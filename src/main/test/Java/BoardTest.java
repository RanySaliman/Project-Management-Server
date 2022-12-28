package Java;

import static org.junit.jupiter.api.Assertions.*;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.User;
import ProjectManagement.entities.UserInBoard;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.entities.enums.UserSource;
import org.junit.jupiter.api.Test;

import java.util.Set;

class BoardTest {

    @Test
    void testAddUser() {
        User user = new User("rany@gmail.com","ranysaliman","123456", UserSource.GITHUB);
        UserInBoard userInBoard = new UserInBoard(user,UserRole.REGISTERED, Set.of());

        Board board = new Board("Test Board");

        board.getUsers().add(userInBoard);
        assertTrue(board.getUsers().contains(userInBoard));
        assertEquals(UserRole.REGISTERED, board.getUser(user.getId()).get().getUserRole());
    }

    @Test
    void testDefaultTaskTypes() {
        Board board = new Board("Test Board");
        assertEquals(3, board.getTaskTypes().size());
        assertTrue(board.getTaskTypes().contains("Task"));
        assertTrue(board.getTaskTypes().contains("Bug"));
        assertTrue(board.getTaskTypes().contains("Subtask"));
    }

    @Test
    void testDefaultStatuses() {
        Board board = new Board("Test Board");
        assertEquals(3, board.getStatuses().size());
        assertTrue(board.getStatuses().contains("Open"));
        assertTrue(board.getStatuses().contains("In Progress"));
        assertTrue(board.getStatuses().contains("Done"));
    }
}
