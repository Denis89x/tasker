package by.lebenkov.task_tracker.storage.dto.userDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("username")
    String username;
}
