package by.lebenkov.task_tracker.api.service;

import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.model.Task;

import java.util.List;

public interface TaskReadService {
    List<TaskResponse> fetchAllTaskResponses();

    List<TaskResponse> fetchAllTasksForAdmin();

    Task findTaskById(Long taskId);
}
