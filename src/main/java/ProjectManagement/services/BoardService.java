package ProjectManagement.services;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;


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
        Board board = new Board(boardName);
        return boardRepository.save(board) ;
    }

    public Response<String> deleteBoard(int boardId){
        boardRepository.deleteById(boardId);
        return Response.createSuccessfulResponse("Board successfully deleted");
    }
}
