package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.mapper.TaskMapper;
import by.lebenkov.task_tracker.api.security.SecurityUtils;
import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
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

    TaskMapper taskMapper;
    TaskRepository taskRepository;
    TaskReadService taskReadService;
    UserReadService userReadService;

    @Override
    @Transactional
    public void createTask(TaskRequest taskRequest) {
        String username = SecurityUtils.getCurrentUsername();

        User owner = userReadService.findUserByUsername(username);

        Task task = taskMapper.toEntity(taskRequest);
        task.setTaskOwner(owner);
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

    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isOwner(#taskId)")
    @Override
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskReadService.findTaskById(taskId);

        taskMapper.updateEntityFromDto(taskRequest, task);
        Task updatedTask = taskRepository.save(task);

        return taskMapper.toResponse(updatedTask);
    }
}