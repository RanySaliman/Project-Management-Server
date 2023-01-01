package Java.ServicesTest;

import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.TaskRepository;
import ProjectManagement.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
//class BoardServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    int taskId;
    Task task;

    @BeforeEach
    public void createTaskSetup() {
        taskId = 1;
        task = new Task();
        task.setId(taskId);
    }

    @Test
    void testGetTaskById_withValidTaskId_returnsCorrectTask() {
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        Optional<Task> result = taskRepository.findById(taskId);
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    void testDeleteTaskById_withValidTaskId_deletesCorrectTask() {
        taskRepository.deleteById(taskId);
        Optional<Task> result = taskRepository.findById(taskId);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteTaskById_withNotValidTaskId_deletesWrongTask() {
        taskRepository.deleteById(taskId+1);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        Optional<Task> result = taskRepository.findById(taskId);
        assertTrue(result.isPresent());
    }

    @Test
    void testAddTaskById_withValidTaskId_returnsCorrectTask() {
        Task savedTask = taskRepository.save(task);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        Optional<Task> result = taskRepository.findById(taskId);
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

//    @Test
//    void testGetBoardById_withInValidBoardId_returnsnNoBoard() {
//        given(boardRepo.findById(2L)).willReturn(Optional.empty());
//        Optional<Board> result = boardService.getBoardById(2L);
//        assertFalse(result.isPresent());
//    }


//    @Test
//    void testAddTask() {
//        Board board = new Board();
//        board.setId(1);
//
//        Task task = new Task(board, 0, 1, 2, 3, "Test Task", "This is a test task", "Open", "Task");
//        Response<Task> response = taskService.addTask(task);
//
//        assertTrue(response.isSuccess());
//        assertEquals(task, response.getData());
//    }

}


