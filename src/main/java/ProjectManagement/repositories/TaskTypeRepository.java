package ProjectManagement.repositories;
import ProjectManagement.entities.TaskType;
import ProjectManagement.entities.CompositeKeyBoardStatusType;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, CompositeKeyBoardStatusType> {

}