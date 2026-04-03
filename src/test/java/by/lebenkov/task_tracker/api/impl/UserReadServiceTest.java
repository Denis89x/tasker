package by.lebenkov.task_tracker.api.impl;

import by.lebenkov.task_tracker.api.service.impl.UserReadServiceImpl;
import by.lebenkov.task_tracker.api.util.exception.ObjectNotFoundException;
import by.lebenkov.task_tracker.storage.dto.userDto.UserResponse;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserReadServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserReadServiceImpl userReadService;

    @Test
    @DisplayName("Должен вернуть юзера по айди, если он существует")
    void findUserByUserId_ShouldReturnUser_WhenUserExists() {
        long userId = 1;

        User mockUser = User.builder()
                .userId(userId)
                .username("username")
                .build();

        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

        User foundedUser = userReadService.findUserByUserId(userId);

        assertNotNull(foundedUser);
        assertEquals(mockUser.getUsername(), foundedUser.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Должен выбросить исключение ObjectNotFoundException если юзера не существует")
    void findUserByUserId_ShouldThrowObjectNotFoundException_WhenUserNotExists() {
        long userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> userReadService.findUserByUserId(userId)
        );

        assertTrue(ex.getMessage().contains("User with " + userId + " id not found!"));
    }

    @Test
    @DisplayName("Должен вернуть юзера по нику")
    void findUserByUsername_ShouldReturnUser_WhenUserExists() {
        String username = "username";

        User mockUser = User.builder()
                .userId(1L)
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        User foundedUser = userReadService.findUserByUsername(username);

        assertNotNull(foundedUser);
        assertEquals(username, foundedUser.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Должен выбросить исключение ObjectNotFoundException когда юзер не найден")
    void findUserByUsername_ShouldThrowObjectNotFoundException_WhenUserNotExists() {
        String username = "username";

        when (userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> userReadService.findUserByUsername(username)
        );

        assertTrue(ex.getMessage().contains("User with '" + username + "' username not found"));
    }

    @Test
    @DisplayName("Должен вернуть смаппенного юзера, если он существует")
    void fetchUserByUserId_ShouldReturnMappedUser_WhenUserExists() {
        User mockUser = User.builder()
                .userId(1L)
                .username("username")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        UserResponse mappedUser = userReadService.fetchUserByUserId(mockUser.getUserId());

        assertNotNull(mappedUser);
        assertEquals(mockUser.getUsername(), mappedUser.getUsername());
        assertEquals(mockUser.getUserId(), mappedUser.getUserId());
        verify(userRepository, times(1)).findById(anyLong());
    }
}