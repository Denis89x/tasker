package by.lebenkov.task_tracker.api.controller;

import by.lebenkov.task_tracker.api.security.JwtUtilService;
import by.lebenkov.task_tracker.api.service.UserCommandService;
import by.lebenkov.task_tracker.api.service.impl.AccountDetailsService;
import by.lebenkov.task_tracker.api.util.exception.GlobalExceptionHandler;
import by.lebenkov.task_tracker.storage.dto.authDto.AuthResponse;
import by.lebenkov.task_tracker.storage.dto.userDto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserCommandService userCommandService;

    @MockitoBean
    private JwtUtilService jwtUtilService;

    @MockitoBean
    private AccountDetailsService accountDetailsService;

    /*@Test
    @DisplayName("Успешный вход в систему")
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        UserRequest request = UserRequest.builder()
                .username("username")
                .password("password")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("secret")
                .build();

        when(userCommandService.authenticate(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("secret"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));;

        verify(userCommandService).authenticate(any(UserRequest.class));
    }

    @Test
    @DisplayName("Ошибка 400 при пустом имени пользователя")
    void authenticate_ShouldReturnBadRequest_WhenUsernameIsEmpty() throws Exception {
        UserRequest request = UserRequest.builder()
                .username("")
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Успешная регистрация")
    void createUser_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        UserRequest request = UserRequest.builder()
                .username("username")
                .password("password")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("secret")
                .build();
        when(userCommandService.registerUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("secret"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userCommandService).registerUser(any(UserRequest.class));
    }

    @Test
    @DisplayName("Ошибка 400 при неверных данных")
    void createUser_ShouldReturnBadRequest_WhenCredentialsAveInvalid() throws Exception {
        UserRequest request = UserRequest.builder()
                .password("")
                .username("")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }*/
}