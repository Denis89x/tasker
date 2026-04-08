package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.security.SecurityUtils;
import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskCommandServiceImpl implements TaskCommandService {

    TaskRepository taskRepository;
    TaskReadService taskReadService;
    UserReadService userReadService;

    private Task convertTaskRequestToTask(TaskRequest taskRequest, User owner) {
        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .taskStatus(taskRequest.getTaskStatus())
                .taskPriority(taskRequest.getTaskPriority())
                .taskOwner(owner)
                .dueDate(taskRequest.getDueDate())
                .build();
    }

    @Override
    @Transactional
    public void createTask(TaskRequest taskRequest) {
        String username = SecurityUtils.getCurrentUsername();

        User owner = userReadService.findUserByUsername(username);

        Task task = convertTaskRequestToTask(taskRequest, owner);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isOwner(#taskId)")
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    private void updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskReadService.findTaskById(taskId);
        task.setTaskStatus(status);
        taskRepository.save(task);
    }

    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isOwner(#taskId)")
    @Override
    public void updateTaskStatusByTaskId(Long taskId, TaskStatus status) {
        updateTaskStatus(taskId, status);
    }
}