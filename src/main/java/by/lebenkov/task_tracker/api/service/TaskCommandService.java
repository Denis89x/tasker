package by.lebenkov.task_tracker.api.service;

import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;

public interface TaskCommandService {
    void createTask(TaskRequest taskRequest);

    void deleteTaskById(Long taskId);

    void updateTaskStatusByTaskId(Long taskId, TaskStatus status);
}