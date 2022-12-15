package ProjectManagement.repositories;

import ProjectManagement.entities.Status;
import ProjectManagement.entities.CompositeKeyBoardStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, CompositeKeyBoardStatusType> {

}
