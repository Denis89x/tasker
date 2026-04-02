package by.lebenkov.task_tracker.api.config;

import by.lebenkov.task_tracker.api.security.SecurityUtils;
import by.lebenkov.task_tracker.storage.repositories.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component("taskSecurity")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskSecurity {

    TaskRepository taskRepository;

    public boolean isOwner(Long taskId) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        return taskRepository.findById(taskId)
                .map(task -> task.getTaskOwner().getUsername().equals(currentUsername))
                .orElse(false);
    }
}
