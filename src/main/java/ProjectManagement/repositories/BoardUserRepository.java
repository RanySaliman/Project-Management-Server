package ProjectManagement.repositories;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.CompositeKeyBoardUser;
import ProjectManagement.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardUserRepository extends JpaRepository<Board, CompositeKeyBoardUser> {

    public Optional<UserRole> findByUserIdAndBoardId(int userId, int boardId);
    public long deleteByBoardId(int boardId);

}
