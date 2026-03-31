package by.lebenkov.task_tracker.storage.repositories;

import by.lebenkov.task_tracker.storage.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTaskOwner_UserId(Long userId);
}
