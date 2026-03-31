package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import by.lebenkov.task_tracker.storage.repositories.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskCommandServiceImpl implements TaskCommandService {

    ModelMapper modelMapper;
    TaskRepository taskRepository;
    TaskReadService taskReadService;
    UserReadService userReadService;

    private Task convertTaskRequestToTask(TaskRequest taskRequest, Long userId) {
        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .taskStatus(taskRequest.getTaskStatus())
                .taskPriority(taskRequest.getTaskPriority())
                .dueDate(taskRequest.getDueDate())
                .taskOwner(userReadService.findUserByUserId(userId))
                .build();
    }

    @Override
    public void createTask(TaskRequest taskRequest, Long userId) {
        taskRepository.save(convertTaskRequestToTask(taskRequest, userId));
    }

    private void updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskReadService.findTaskById(taskId);
        task.setTaskStatus(status);
        taskRepository.save(task);
    }

    @Override
    public void updateTaskStatusByTaskId(Long taskId, TaskStatus status) {
        updateTaskStatus(taskId, status);
    }
}