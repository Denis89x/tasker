package by.lebenkov.task_tracker.api.impl;

import by.lebenkov.task_tracker.api.service.impl.AccountDetailsService;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountDetailsService accountDetailsService;

    @Test
    @DisplayName("Должен вернуть UserDetails, если пользователь найден")
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {

        String username = "testUser";
        User user = User.builder()
                .username(username)
                .password("enc_pass")
                .role("ROLE_USER")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails result = accountDetailsService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Должен дропнуть исключение, если пользователь не найден")
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> accountDetailsService.loadUserByUsername(username)
        );

        assertTrue(ex.getMessage().contains("User not found: " + username));
    }
}
