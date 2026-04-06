package by.lebenkov.task_tracker.api.impl;

import by.lebenkov.task_tracker.api.security.JwtUtilService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.api.service.impl.AccountDetailsService;
import by.lebenkov.task_tracker.api.service.impl.UserCommandServiceImpl;
import by.lebenkov.task_tracker.storage.dto.authDto.AuthResponse;
import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;
import by.lebenkov.task_tracker.storage.enums.TokenStatus;
import by.lebenkov.task_tracker.storage.model.Token;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.TokenRepository;
import by.lebenkov.task_tracker.storage.repositories.UserRepository;
import org.hibernate.mapping.Any;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    @DisplayName("Успешная аутентификация")
    void authenticate_ShouldAuthUser_WhenCredentialsAreValid() {
        String username = "username";
        String password = "password";
        User user = User.builder()
                .userId(1L)
                .username(username)
                .password(password)
                .build();
        String newAccess = "access";
        String newRefresh = "refresh";

        UserRequest userRequest = UserRequest.builder()
                .username(username)
                .password(password)
                .build();

        UserDetails mockDetails = mock(UserDetails.class);
        when(mockDetails.getUsername()).thenReturn(username);

        when(accountDetailsService.loadUserByUsername(username)).thenReturn(mockDetails);
        when(userReadService.findUserByUsername(username)).thenReturn(user);
        when(jwtUtilService.generateAccessToken(any())).thenReturn(newAccess);
        when(jwtUtilService.generateRefreshToken(any())).thenReturn(newRefresh);

        AuthResponse authResponse = userCommandService.authenticate(userRequest);

        assertNotNull(authResponse);
        assertEquals(newAccess, authResponse.getAccessToken());
        assertEquals(newRefresh, authResponse.getRefreshToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository).findAllValidTokenByUser(user.getUserId());
        verify(tokenRepository, times(2)).save(any(Token.class));
    }

    @Test
    @DisplayName("Успешная регистрация")
    void registerUser_ShouldRegisterUser_WhenCredentialsAreValid() {
        String username = "username";
        String password = "password";
        String hashPass = "hashPass";

        String newAccess = "access";
        String newRefresh = "refresh";

        UserRequest userRequest = UserRequest.builder()
                .username(username)
                .password(password)
                .build();

        when(passwordEncoder.encode(password)).thenReturn(hashPass);
        when(jwtUtilService.generateAccessToken(any())).thenReturn(newAccess);
        when(jwtUtilService.generateRefreshToken(any())).thenReturn(newRefresh);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse response = userCommandService.registerUser(userRequest);

        assertNotNull(response);
        assertEquals(newAccess, response.getAccessToken());
        assertEquals(newRefresh, response.getRefreshToken());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(hashPass, userCaptor.getValue().getPassword());

        verify(tokenRepository, times(2)).save(any(Token.class));
    }

    @Test
    @DisplayName("Успешный рефреш токена")
    void refreshToken_ShouldRefreshToken() {
        String username = "username";
        String oldToken = "old_token";
        String newAccess = "access";
        String newRefresh = "refresh";

        User user = User.builder()
                .userId(1L)
                .username(username)
                .build();

        Token token = Token.builder()
                .token(oldToken)
                .revoked(false)
                .expired(false)
                .tokenStatus(TokenStatus.REFRESH)
                .build();

        when(jwtUtilService.extractUsername(anyString())).thenReturn(username);
        when(userReadService.findUserByUsername(username)).thenReturn(user);
        when(tokenRepository.findByToken(oldToken)).thenReturn(Optional.of(token));
        when(jwtUtilService.generateAccessToken(any())).thenReturn(newAccess);
        when(jwtUtilService.generateRefreshToken(any())).thenReturn(newRefresh);

        AuthResponse response = userCommandService.refreshToken(oldToken);

        assertNotNull(response);
        assertEquals(newAccess, response.getAccessToken());
        assertEquals(newRefresh, response.getRefreshToken());

        verify(tokenRepository).findAllValidTokenByUser(user.getUserId());
        verify(tokenRepository, times(2)).save(any(Token.class));
    }
}