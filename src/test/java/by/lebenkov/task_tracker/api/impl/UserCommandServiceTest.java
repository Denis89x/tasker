package by.lebenkov.task_tracker.api.impl;

import by.lebenkov.task_tracker.api.security.JwtUtilService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.api.service.impl.AccountDetailsService;
import by.lebenkov.task_tracker.api.service.impl.UserCommandServiceImpl;
import by.lebenkov.task_tracker.storage.dto.authDto.AuthResponse;
import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;
import by.lebenkov.task_tracker.storage.model.Token;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.TokenRepository;
import by.lebenkov.task_tracker.storage.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserReadService userReadService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtilService jwtUtilService;

    @Mock
    private AccountDetailsService accountDetailsService;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @Test
    @DisplayName("Авторизирует пользователя и возвращает токен, если его данные верны")
    void authenticate_ShouldAuthUser_WhenCredentialsAreValid() {
        UserRequest request = UserRequest.builder()
                .username("username")
                .password("password")
                .build();

        String expectedToken = "mock-jwt-token";

        Token mockToken = Token.builder()
                .token(expectedToken)
                .build();

/*        Тут лютая хуйня с тестом, нужно его переделать, ща додуматься не могу*/

        User mockUser = User.builder()
                .username("username")
                .tokenList(List.of(mockToken))
                .build();

        UserDetails mockDetails = mock(UserDetails.class);


        when(userReadService.findUserByUsername(mockUser.getUsername())).thenReturn(mockUser);
        when(accountDetailsService.loadUserByUsername(request.getUsername())).thenReturn(mockDetails);
        when(jwtUtilService.generateToken(mockDetails)).thenReturn(expectedToken);

        AuthResponse response = userCommandService.authenticate(request);

        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());

        verify(tokenRepository, times(1)).save(mockToken);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void registerUser_ShouldRegisterUser_WhenCredentialsAreValid() {
        UserRequest request = UserRequest.builder()
                .username("username")
                .password("password")
                .build();

        String expectedToken = "mock-jwt-token";

        when(jwtUtilService.generateToken(any(UserDetails.class))).thenReturn(expectedToken);

        AuthResponse response = userCommandService.registerUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals(expectedToken, response.getToken());
        assertNotEquals(request.getPassword(), capturedUser.getPassword());
        assertEquals(request.getUsername(), capturedUser.getUsername());
        verify(passwordEncoder).encode(request.getPassword());
    }
}