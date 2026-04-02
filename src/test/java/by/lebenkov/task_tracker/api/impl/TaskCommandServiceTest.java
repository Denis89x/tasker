package by.lebenkov.task_tracker.api.impl;

import by.lebenkov.task_tracker.api.security.SecurityUtils;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.service.UserReadService;
import by.lebenkov.task_tracker.api.service.impl.TaskCommandServiceImpl;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import by.lebenkov.task_tracker.storage.model.Task;
import by.lebenkov.task_tracker.storage.model.User;
import by.lebenkov.task_tracker.storage.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskCommandServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserReadService userReadService;

    @Mock
    private TaskReadService taskReadService;

    @InjectMocks
    private TaskCommandServiceImpl taskCommandService;

    @Test
    void createTask_ShouldSaveTask_WhenUserExists() {
        String username = "user";
        User mockUser = User.builder()
                .username(username)
                .build();

        TaskRequest request = TaskRequest.builder()
                .title("Title")
                .build();

        try (MockedStatic<SecurityUtils> mockedSecurity = mockStatic(SecurityUtils.class)) {
            mockedSecurity.when(SecurityUtils::getCurrentUsername).thenReturn(username);

            when(userReadService.findUserByUsername(username)).thenReturn(mockUser);

            taskCommandService.createTask(request);

            ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
            verify(taskRepository).save(taskCaptor.capture());

            Task capturedTask = taskCaptor.getValue();

            assertEquals("Title", capturedTask.getTitle());
            assertEquals(mockUser, capturedTask.getTaskOwner());
        }
    }

    @Test
    void updateTaskStatusByTaskId_ShouldUpdateTaskStatus_WhenTaskFounded() {
        long taskId = 1;
        TaskStatus mockStatus = TaskStatus.TASK_DONE;
        String title = "Stay the same";

        Task mockTask = Task.builder()
                .taskId(taskId)
                .title(title)
                .taskStatus(TaskStatus.NEW_TASK)
                .build();

        when(taskReadService.findTaskById(taskId)).thenReturn(mockTask);

        taskCommandService.updateTaskStatusByTaskId(taskId, mockStatus);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());

        Task capturedTask = taskCaptor.getValue();

        assertEquals(title, capturedTask.getTitle());
        assertEquals(mockStatus, capturedTask.getTaskStatus());
        verifyNoMoreInteractions(taskRepository);
    }
}