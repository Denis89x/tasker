package by.lebenkov.task_tracker.storage.repositories;

import by.lebenkov.task_tracker.storage.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query(value = "SELECT * FROM task WHERE task_id = :id", nativeQuery = true)
    Optional<Task> findDeletedTaskById(@Param("id") Long id);
}
