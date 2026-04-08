package by.lebenkov.task_tracker.api.service.impl;

import by.lebenkov.task_tracker.api.mapper.UserMapper;
import by.lebenkov.task_tracker.api.security.AccountDetails;
import by.lebenkov.task_tracker.api.security.JwtUtilService;
import by.lebenkov.task_tracker.api.service.UserCommandService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.api.util.exception.ObjectNotFoundException;
import by.lebenkov.task_tracker.storage.dto.authDto.AuthResponse;
import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;
import by.lebenkov.task_tracker.storage.enums.TokenStatus;
import by.lebenkov.task_tracker.storage.enums.TokenType;
import by.lebenkov.task_tracker.storage.model.Token;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.TokenRepository;
import by.lebenkov.task_tracker.storage.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCommandServiceImpl implements UserCommandService {

    UserRepository userRepository;
    UserReadService userReadService;
    AuthenticationManager authenticationManager;
    JwtUtilService jwtUtilService;
    AccountDetailsService accountDetailsService;
    TokenRepository tokenRepository;
    UserMapper userMapper;

    private void saveUserToken(User user, String jwtToken, TokenStatus status) {
        var token = Token.builder()
                .user(user)
                .tokenStatus(status)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private void validateToken(Token token) {
        if (token.isRevoked() || token.isExpired() ||
                token.getTokenStatus() != TokenStatus.REFRESH) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtUtilService.extractUsername(refreshToken);

        User user = userReadService.findUserByUsername(username);

        var storedToken = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ObjectNotFoundException("Token not found"));

        validateToken(storedToken);

        var userDetails = new AccountDetails(user);
        String newAccessToken = jwtUtilService.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtilService.generateRefreshToken(userDetails);

        revokeAllUserTokens(user);
        saveUserToken(user, newAccessToken, TokenStatus.ACCESS);
        saveUserToken(user, newRefreshToken, TokenStatus.REFRESH);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public AuthResponse registerUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        userRepository.save(user);

        var userDetails = new AccountDetails(user);

        String accessToken = jwtUtilService.generateAccessToken(userDetails);
        String refreshToken = jwtUtilService.generateRefreshToken(userDetails);

        saveUserToken(user, accessToken, TokenStatus.ACCESS);
        saveUserToken(user, refreshToken, TokenStatus.REFRESH);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(UserRequest userRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
        );

        UserDetails accountDetails = accountDetailsService.loadUserByUsername(userRequest.getUsername());

        String accessToken = jwtUtilService.generateAccessToken(accountDetails);
        String refreshToken = jwtUtilService.generateRefreshToken(accountDetails);

        User user = userReadService.findUserByUsername(accountDetails.getUsername());

        revokeAllUserTokens(user);

        saveUserToken(user, accessToken, TokenStatus.ACCESS);
        saveUserToken(user, refreshToken, TokenStatus.REFRESH);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}