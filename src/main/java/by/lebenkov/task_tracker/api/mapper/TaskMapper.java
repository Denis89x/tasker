package by.lebenkov.task_tracker.api.mapper;

import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequest request);

    @Mapping(target = "taskOwnerUsername", source = "taskOwner.username")
    TaskResponse toResponse(Task task);

    List<TaskResponse> toResponseList(List<Task> tasks);
}
