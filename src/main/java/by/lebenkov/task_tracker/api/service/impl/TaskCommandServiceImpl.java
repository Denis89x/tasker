package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import by.lebenkov.task_tracker.storage.model.User;
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

    ModelMapper modelMapper; // Внедрил зависимость ModelMapper, создал конфигурационный файл для него, который делает его бином, для будущей логики, насколько полезно?
    TaskRepository taskRepository;
    TaskReadService taskReadService;

    private Task convertTaskRequestToTask(TaskRequest taskRequest) {
        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .taskStatus(taskRequest.getTaskStatus())
                .taskPriority(taskRequest.getTaskPriority())
                .dueDate(taskRequest.getDueDate())
                .taskOwner(User.builder().build()) // Задаю тут пустого юзера, потому что сейчас нам неоткуда его брать, или же я пока не могу придумать как это сделать
                .build();
    }

    @Override
    public void createTask(TaskRequest taskRequest) {
        taskRepository.save(convertTaskRequestToTask(taskRequest)); // Вот тут думаю, стоит ли переносить метод save в отдельный service для сохранения, или это уже лишнее?
    }

    private void updateTaskStatus(Long taskId, TaskStatus status) { // этот метот мне не особо нравится, он будто делает слишком много действий, и его можно разбить, но стоит ли это делать?
        Task task = taskReadService.findTaskById(taskId);
        task.setTaskStatus(status);
        taskRepository.save(task);
    }

    @Override
    public void updateTaskStatusByTaskId(Long taskId, TaskStatus status) {
        updateTaskStatus(taskId, status);
    }
}