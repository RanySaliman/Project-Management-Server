import ProjectManagement.SpringApp;
import ProjectManagement.controllers.BoardController;
import ProjectManagement.controllers.entities.FilterFields;
import ProjectManagement.entities.Board;
import ProjectManagement.entities.Task;
import ProjectManagement.services.BoardService;
import ProjectManagement.services.TaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@SpringBootTest(classes = SpringApp.class)
public class BoardControllerTest {
    @Autowired
    BoardController boardController;

    @MockBean
    BoardService boardService;
    @MockBean
    TaskService taskService;
    static Task task;
    static Board board;
    static List<Task> tasks;
    static FilterFields filterFields;


    @BeforeAll
    static void setup() {
        board = new Board("bordi");
        board.setId(1);
        tasks = new ArrayList<Task>();
        task = new Task(1, 2, 1, 2, "first", "2");
        tasks.add(task);
        filterFields = new FilterFields();
        filterFields.setBoardId(1);
        filterFields.setImportance(2);
    }

    @Test
    public void filter_Successfully(){
        when(taskService.filter(filterFields.getBoardId(),filterFields)).thenReturn(null);
        //to be continued
    }
}