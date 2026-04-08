package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.security.SecurityUtils;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.api.util.exception.ObjectNotFoundException;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import by.lebenkov.task_tracker.storage.repositories.TaskRepository;
import by.lebenkov.task_tracker.storage.repositories.specification.TaskSpecifications;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskReadServiceImpl implements TaskReadService {

    TaskRepository taskRepository;
    UserReadService userReadService;

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

    @Override
    public Page<TaskResponse> fetchAllTaskResponses(TaskStatus status, Integer priority, Pageable pageable) {
        Long userId = userReadService.findUserByUsername(SecurityUtils.getCurrentUsername()).getUserId();

        Specification<Task> spec = Specification.where(TaskSpecifications.hasOwnerId(userId))
                .and(TaskSpecifications.hasStatus(status))
                .and(TaskSpecifications.hasPriority(priority));

        return taskRepository.findAll(spec, pageable)
                .map(this::convertTaskToTaskResponse);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<TaskResponse> fetchAllTasksForAdmin(TaskStatus status, Integer priority, Pageable pageable) {
        Specification<Task> spec = Specification
                .where(TaskSpecifications.hasStatus(status))
                .and(TaskSpecifications.hasPriority(priority));

        return taskRepository.findAll(spec, pageable)
                .map(this::convertTaskToTaskResponse);
    }

    @Override
    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Task with id {} not found", taskId);
            return new ObjectNotFoundException("Task with " + taskId + " id not found!");
        });
    }
}