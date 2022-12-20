package ProjectManagement.services;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.enums.UserRole;
import ProjectManagement.repositories.BoardRepository;
import ProjectManagement.repositories.BoardUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardUserRepository boardUserRepository;

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    /**
     * method that responsible for fetching board for specific board id
     * @param id
     * @return board
     */
    public Response<Board> getBoard(int id) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()){
            return Response.createFailureResponse("");
        }else {
            return Response.createSuccessfulResponse(board.get());
        }
    }

    public Board createBoard(String boardName){
        return boardRepository.save(new Board(boardName));
    }

    public Response<String> deleteBoard(int boardId){
        boardUserRepository.deleteByBoardId(boardId);
        boardRepository.deleteById(boardId);
        return Response.createSuccessfulResponse("Board successfully deleted");
    }

}
