package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.security.AccountDetails;
import by.lebenkov.task_tracker.api.security.JwtUtilService;
import by.lebenkov.task_tracker.api.service.UserCommandService;
import by.lebenkov.task_tracker.storage.dto.authDto.AuthResponse;
import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCommandServiceImpl implements UserCommandService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtUtilService jwtUtilService;
    AccountDetailsService accountDetailsService;

    private User convertUserRequestToUser(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role("ROLE_USER")
                .build();
    }

    @Override
    public AuthResponse registerUser(UserRequest userRequest) {
        User user = convertUserRequestToUser(userRequest);
        userRepository.save(user);

        String jwtToken = jwtUtilService.generateToken(new AccountDetails(user));

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(UserRequest userRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
        );

        UserDetails accountDetails = accountDetailsService.loadUserByUsername(userRequest.getUsername());

        String jwtToken = jwtUtilService.generateToken(accountDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}