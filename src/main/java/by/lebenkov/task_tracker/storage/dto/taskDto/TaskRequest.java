package by.lebenkov.task_tracker.storage.dto.taskDto;

import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequest {

    @NotBlank(message = "Title should not be empty")
    @JsonProperty("title")
    String title;

    @JsonProperty("description")
    String description;

    @JsonProperty("task_status")
    TaskStatus taskStatus;

    @JsonProperty("due_date")
    LocalDateTime dueDate;

    @JsonProperty("task_priority")
    Integer taskPriority;
}