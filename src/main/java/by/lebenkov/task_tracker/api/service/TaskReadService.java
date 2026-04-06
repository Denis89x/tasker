package by.lebenkov.task_tracker.api.service;

import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskReadService {
    public Page<TaskResponse> fetchAllTaskResponses(TaskStatus status, Integer priority, Pageable pageable);

    List<TaskResponse> fetchAllTasksForAdmin();

    Task findTaskById(Long taskId);
}
