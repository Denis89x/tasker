package by.lebenkov.task_tracker.api.service;

import by.lebenkov.task_tracker.storage.dto.authDto.AuthResponse;
import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;

public interface UserCommandService {
    AuthResponse registerUser(UserRequest userRequest);

    AuthResponse authenticate (UserRequest userRequest);

    AuthResponse refreshToken(String refreshToken);
}
