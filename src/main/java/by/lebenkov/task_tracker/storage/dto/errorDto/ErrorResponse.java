package by.lebenkov.task_tracker.storage.dto.errorDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ErrorResponse {

    int status;

    String error;

    String path;

    String message;

    LocalDateTime timestamp;
}
