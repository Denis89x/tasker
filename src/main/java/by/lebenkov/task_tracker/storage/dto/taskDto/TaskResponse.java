package by.lebenkov.task_tracker.storage.dto.taskDto;

import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse {

    @JsonProperty("task_id")
    Long taskId;

    @JsonProperty("title")
    String title;

    @JsonProperty("description")
    String description;

    @JsonProperty("task_status")
    TaskStatus taskStatus;

    @JsonProperty("task_priority")
    Integer taskPriority;

    @JsonProperty("due_date")
    LocalDateTime dueDate;

    @JsonProperty("task_owner_username")
    String taskOwnerUsername;
}