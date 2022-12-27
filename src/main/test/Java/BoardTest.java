package Java;

import static org.junit.jupiter.api.Assertions.*;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.UserRole;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void testAddUser() {
        User user = new User();

        Board board = new Board("Test Board");

        board.addUser(user, UserRole.REGISTERED);
        assertTrue(board.getUsers().containsKey(user));
        assertEquals(UserRole.REGISTERED, board.getUsers().get(user));
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
