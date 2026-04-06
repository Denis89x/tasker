package by.lebenkov.task_tracker.api.controller;

import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @PostMapping
    public ResponseEntity<Void> createTask(
            @RequestBody @Valid TaskRequest taskRequest) {
        taskCommandService.createTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> fetchAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Integer priority,
            @PageableDefault(sort = "taskId", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(taskReadService.fetchAllTaskResponses(status, priority, pageable));
    }

    @DeleteMapping("/{task_id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("task_id") long taskId) {
        taskCommandService.deleteTaskById(taskId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}