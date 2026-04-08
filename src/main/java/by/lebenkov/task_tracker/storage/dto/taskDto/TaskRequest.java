package by.lebenkov.task_tracker.storage.dto.taskDto;

import by.lebenkov.task_tracker.api.validation.FutureOrPresent;
import by.lebenkov.task_tracker.api.validation.OnCreate;
import by.lebenkov.task_tracker.api.validation.TaskStatusSelection;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@TaskStatusSelection(groups = OnCreate.class)
public class TaskRequest {

    @NotBlank(message = "Title should not be empty")
    @JsonProperty("title")
    String title;

    @JsonProperty("description")
    String description;

    @JsonProperty("task_status")
    TaskStatus taskStatus;

    @FutureOrPresent(message = "Дедлайн не может быть в прошлом")
    @JsonProperty("due_date")
    LocalDateTime dueDate;

    @JsonProperty("task_priority")
    Integer taskPriority;
}