package by.lebenkov.task_tracker.api.controller;

import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    TaskReadService taskReadService;
    TaskCommandService taskCommandService;

    @PostMapping("/{user_id}")
    public ResponseEntity<Void> createTask(
            @RequestBody @Valid TaskRequest taskRequest,
            @PathVariable("user_id") long userId) {
        taskCommandService.createTask(taskRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<TaskResponse>> fetchTaskByUserUserId(
            @PathVariable("user_id") long userId) {
        return ResponseEntity.ok(taskReadService.fetchAllTaskResponsesByUserId(userId));
    }
}