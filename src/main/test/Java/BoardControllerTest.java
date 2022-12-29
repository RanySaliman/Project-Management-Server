package Java;

import ProjectManagement.SpringApp;
import ProjectManagement.controllers.BoardController;
import ProjectManagement.controllers.entities.TaskFields;
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.repositories.TaskRepository;
import ProjectManagement.services.BoardService;
import ProjectManagement.services.TaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SpringBootTest(classes = SpringApp.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BoardControllerTest {
    @Autowired
    BoardController boardController;

    @MockBean
    BoardService boardService;
    @Autowired
    TaskService taskService;
    static Task task;
    static Board board;
    static List<Task> tasks;
    static TaskFields taskFields;

  @Autowired
    BoardRepository boardRepository;

    @MockBean
    TaskRepository taskRepository;

    @BeforeAll
     void setup() {
        board = new Board("bordi");
        board.setId(1);

        board = boardRepository.save(board);
        System.out.println(boardRepository.getBoardById(board.getId()).get());
        tasks = new ArrayList<Task>();
        task = new Task(board, 2, 1, 2,3, "first", "2",board.getStatuses().stream().findFirst().get()
                    ,board.getTaskTypes().stream().findFirst().get());
        Task save = taskRepository.save(task);
        System.out.println(save);
        taskFields = new TaskFields();
        taskFields.setImportance(2);
        System.out.println(boardController.filter(board,taskFields));


    }

    @Test
    public void filter_Successfully(){
        boardController.filter(board, taskFields);

    }
}