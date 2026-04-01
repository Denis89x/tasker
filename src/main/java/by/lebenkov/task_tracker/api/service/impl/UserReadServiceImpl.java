package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.api.util.exception.ObjectNotFoundException;
import by.lebenkov.task_tracker.storage.dto.userDto.UserResponse;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserReadServiceImpl implements UserReadService {

    UserRepository userRepository;

    private UserResponse convertUserToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();
    }

    @Override
    public User findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new ObjectNotFoundException("User with " + userId + " id not found!");
        });
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User with username {} not found", username);
            return new ObjectNotFoundException("User with '" + username + "' username not found");
        });
    }

    @Override
    public UserResponse fetchUserByUserId(Long userId) {
        return convertUserToUserResponse(findUserByUserId(userId));
    }
}