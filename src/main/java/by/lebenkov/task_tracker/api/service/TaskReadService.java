package by.lebenkov.task_tracker.api.service;

import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskReadService {
    Page<TaskResponse> fetchAllTaskResponses(TaskStatus status, Integer priority, Pageable pageable);

    Page<TaskResponse> fetchAllTasksForAdmin(TaskStatus status, Integer priority, Pageable pageable);

    Task findTaskById(Long taskId);

    TaskResponse fetchTaskById(Long taskId);
}
