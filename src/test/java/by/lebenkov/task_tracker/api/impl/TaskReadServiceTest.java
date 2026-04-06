package by.lebenkov.task_tracker.api.impl;

import by.lebenkov.task_tracker.api.security.SecurityUtils;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.api.service.impl.TaskReadServiceImpl;
import by.lebenkov.task_tracker.api.util.exception.ObjectNotFoundException;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.model.Task;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskReadServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserReadService userReadService;

    @InjectMocks
    private TaskReadServiceImpl taskReadService;

    @Test
    @DisplayName("Должен вернуть таску если она существует")
    void findTaskById_ShouldReturnTask_WhenTaskExists() {
        long taskId = 1;
        String title = "title";

        Task mockTask = Task.builder()
                .title(title)
                .taskId(taskId)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        Task foundedTask = taskReadService.findTaskById(taskId);

        assertNotNull(foundedTask);
        assertEquals(taskId, foundedTask.getTaskId());
        assertEquals(title, foundedTask.getTitle());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Должен выбросить ObjectNotFoundException исключение если таски не существует")
    void findTaskById_ShouldThrowException_WhenTaskNotExists() {
        long taskId = 1;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> taskReadService.findTaskById(taskId)
        );

        assertTrue(ex.getMessage().contains("Task with " + taskId + " id not found!"));
    }

    @Test
    @DisplayName("Должен вернуть список тасков сконвертированных в Dto")
    void fetchAllTasksForAdmin_ShouldReturnMappedList() {
        User mockUser = User.builder()
                .userId(1L)
                .username("username")
                .build();

        Task mockTask = Task.builder()
                .title("title")
                .taskOwner(mockUser)
                .build();

        when(taskRepository.findAll()).thenReturn(List.of(mockTask));

        List<TaskResponse> foundedTaskResponses = taskReadService.fetchAllTasksForAdmin();

        assertEquals(1, foundedTaskResponses.size());
        assertNotNull(foundedTaskResponses);
        assertEquals(mockUser.getUsername(), foundedTaskResponses.get(0).getTaskOwnerUsername());
        assertEquals(mockTask.getTitle(), foundedTaskResponses.get(0).getTitle());
    }

    /*@Test
    @DisplayName("Должен вернуть смапенный список всех тасков конкретного юзера")
    void fetchAllTaskResponses_ShouldReturnMappedList() {
        String username = "user";

        User mockUser = User.builder()
                .userId(1L)
                .username(username)
                .build();

        Task task = Task.builder()
                .title("title")
                .taskOwner(mockUser)
                .build();

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(username);

            when(userReadService.findUserByUsername(username)).thenReturn(mockUser);
            when(taskRepository.findAllByTaskOwner_UserId(mockUser.getUserId())).thenReturn(List.of(task));

            List<TaskResponse> result = taskReadService.fetchAllTaskResponses();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(username, result.get(0).getTaskOwnerUsername());
            assertEquals(task.getTitle(), result.get(0).getTitle());
        }
    }*/
}