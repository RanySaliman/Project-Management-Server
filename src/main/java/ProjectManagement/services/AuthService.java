package ProjectManagement.services;

import ProjectManagement.entities.Board;
import ProjectManagement.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private BoardRepository boardRepository;

    /**
     * method that responsible for finding board by id
     * @param id
     * @return  board if the id exits
     */
    public Optional<Board> findById(int id) {
        return boardRepository.getBoardById(id);
//        return Optional.empty();
    }
}
