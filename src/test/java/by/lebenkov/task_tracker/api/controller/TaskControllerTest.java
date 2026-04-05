package by.lebenkov.task_tracker.api.controller;

import by.lebenkov.task_tracker.api.security.JwtUtilService;
import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.service.impl.AccountDetailsService;
import by.lebenkov.task_tracker.api.util.exception.GlobalExceptionHandler;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TaskController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskReadService taskReadService;

    @MockitoBean
    private TaskCommandService taskCommandService;

    @MockitoBean
    private JwtUtilService jwtUtilService;

    @MockitoBean
    private AccountDetailsService accountDetailsService;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Успешное создание таски")
    void createTask_ShouldCreateTask_WhenCredentialsAreValid() throws Exception {
        TaskRequest request = TaskRequest.builder()
                .title("title")
                .taskStatus(TaskStatus.NEW_TASK)
                .description("desc")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(taskCommandService, times(1)).createTask(any());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Ошибка 400 при неверных параметрах")
    void createTask_ShouldReturnBadRequest_WhenCredentialsAreInvalid() throws Exception {
        TaskRequest request = TaskRequest.builder()
                .title("")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Успешное возвращение списка тасков для авторизированного юзера")
    void fetchAllTasks_ShouldReturnListOfTasks() throws Exception {
        TaskResponse response = TaskResponse.builder()
                .title("title")
                .taskOwnerUsername("user")
                .build();

        when(taskReadService.fetchAllTaskResponses()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/tasks")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("title"));;
    }

    @Test
    @DisplayName("Ошибка 403 неавторизован")
    void fetchAllTasks_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Успешное удаление таски")
    void deleteTask_ShouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk());
    }
}