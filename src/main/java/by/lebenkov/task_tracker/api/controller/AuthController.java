package by.lebenkov.task_tracker.api.controller;

import by.lebenkov.task_tracker.api.service.UserCommandService;
import by.lebenkov.task_tracker.storage.dto.authDto.AuthResponse;
import by.lebenkov.task_tracker.storage.dto.authDto.RefreshTokenRequest;
import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    UserCommandService userCommandService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(
            @RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userCommandService.registerUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userCommandService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(userCommandService.refreshToken(request.getRefreshToken()));
    }
}
