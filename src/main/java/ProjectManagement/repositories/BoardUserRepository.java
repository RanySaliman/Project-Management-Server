package ProjectManagement.repositories;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.BoardToUser;
import ProjectManagement.entities.CompositeKeyBoardUser;
import ProjectManagement.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardUserRepository extends JpaRepository<BoardToUser, CompositeKeyBoardUser> {
    Optional<BoardToUser> findByUserIdAndBoardId( int userId, int boardId);
    long deleteByBoardId( int boardId);

}
