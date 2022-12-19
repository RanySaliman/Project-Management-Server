package ProjectManagement.repositories;

import ProjectManagement.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer> {

    Optional<Board> getBoardById(int id);


}
