package by.lebenkov.task_tracker.api.service;

import by.lebenkov.task_tracker.storage.dto.userDto.UserResponse;
import by.lebenkov.task_tracker.storage.model.User;

public interface UserReadService {
    User findUserByUserId(Long userId);

    UserResponse fetchUserByUserId(Long userId);
}
