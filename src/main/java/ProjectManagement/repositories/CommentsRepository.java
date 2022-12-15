package ProjectManagement.repositories;

import ProjectManagement.entities.Comment;
import ProjectManagement.entities.CompositeKeyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, CompositeKeyComment> {

}
