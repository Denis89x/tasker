package by.lebenkov.task_tracker.storage.dto.userDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @NotBlank(message = "username should not be empty")
    @JsonProperty("username")
    String username;

    @NotBlank(message = "password should not be empty")
    @JsonProperty("password")
    String password;
}