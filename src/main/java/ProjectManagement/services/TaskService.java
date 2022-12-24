package ProjectManagement.services;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Status;
import ProjectManagement.entities.Task;
import ProjectManagement.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    BoardRepository boardRepository;
    /*
    private int boardId;
    @Id
    private int id;
    private int taskParentId;
    private int creator = dani;
    private int assignedUserId ;
    LocalDateTime DueDate;
    int importance; // 1-5 where 5 is the highest priority
    String title;
    String description;*/

    public List<Task> filter(int boardId, Optional<Integer> importance){
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if(optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            return board.getTaskStatusMap().keySet().stream().filter(task -> {
                if (importance.isPresent()) {
                    return task.getImportance() == importance.get();
                } else {
                    return true;
                }
            }).collect(Collectors.toList());
        }
        return null;
    }

    public Response<Task> addTask(int boardId, int taskParentId, int assignedUserId, int importance, String title, String description){
        Task task = new Task(boardId, taskParentId, assignedUserId, importance, title, description);
        Board board = boardRepository.findById(boardId).get();
        board.getTaskStatusMap().put(task,new Status());
        boardRepository.save(board);
        return Response.createSuccessfulResponse(board.getTaskStatusMap().keySet().stream().filter(t -> t.getId() == task.getId()).findFirst().get());
    }

        /*
            private int boardId;
    @Id
    private int id;
    private int taskParentId;
    private int creator;
    private int assignedUserId;
    LocalDateTime DueDate;
    int importance; // 1-5 where 5 is the highest priority
    String title;
    String description;


         */
}
