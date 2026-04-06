package by.lebenkov.task_tracker.storage.repositories.specification;

import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> hasOwnerId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("taskOwner").get("userId"), userId);
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("taskStatus"), status);
    }

    public static Specification<Task> hasPriority(Integer priority) {
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("taskPriority"), priority);
    }
}
