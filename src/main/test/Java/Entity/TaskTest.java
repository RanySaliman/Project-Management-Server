package Java.Entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.BoardRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class TaskTest {

// commiting push
    @Test
    void testTaskConstructor() {
        Board board = new Board();
        board.setId(1);

        Task task = new Task(board, 0, 1, 2, 3, "Test Task", "This is a test task", "Open", "Task");
        assertEquals(board, task.getBoard());
        assertEquals(0, task.getTaskParentId());
        assertEquals(1, task.getCreator());
        assertEquals(2, task.getAssignedUserId());
        assertEquals(3, task.getImportance());
        assertEquals("Test Task", task.getTitle());
        assertEquals("This is a test task", task.getDescription());
        assertEquals("Open", task.getStatus());
        assertEquals("Task", task.getType());
        assertNotNull(task.getDueDate());
    }

    @Test
    void testSetBoard() {
        Task task = new Task();
        Board board = new Board();
        board.setId(1);
        task.setBoard(board);
        assertEquals(board, task.getBoard());
    }

    @Test
    void testSetTaskParentId() {
        Task task = new Task();
        task.setTaskParentId(123);
        assertEquals(123, task.getTaskParentId());
    }

    @Test
    void testSetCreator() {
        Task task = new Task();
        task.setCreator(456);
        assertEquals(456, task.getCreator());
    }

    @Test
    void testSetAssignedUserId() {
        Task task = new Task();
        task.setAssignedUserId(789);
        assertEquals(789, task.getAssignedUserId());
    }
}


