package ProjectManagement.repositories;

import ProjectManagement.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task getTaskById(int id);
}

