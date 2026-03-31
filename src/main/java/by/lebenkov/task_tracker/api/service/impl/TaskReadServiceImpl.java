package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.util.exc.ObjectNotFoundException;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.model.Task;
import by.lebenkov.task_tracker.storage.repositories.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskReadServiceImpl implements TaskReadService {

    TaskRepository taskRepository;

    private TaskResponse convertTaskToTaskResponse(Task task) {
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskStatus(task.getTaskStatus())
                .taskPriority(task.getTaskPriority())
                .dueDate(task.getDueDate())
                .taskOwnerUsername(task.getTaskOwner().getUsername())
                .build();
    }

    private List<Task> fetchAllTasksByUserId(Long userId) {
        return taskRepository.findAllByTaskOwner_UserId(userId);
    }

    @Override
    public List<TaskResponse> fetchAllTaskResponsesByUserId(Long userId) {
        return fetchAllTasksByUserId(userId).stream()
                .map(this::convertTaskToTaskResponse)
                .toList();
    }

    @Override
    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Task with id {} not found", taskId);
            return new ObjectNotFoundException("Task with " + taskId + " id not found!");
        });
    }
}