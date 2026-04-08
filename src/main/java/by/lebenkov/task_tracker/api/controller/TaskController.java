package by.lebenkov.task_tracker.api.controller;

import by.lebenkov.task_tracker.api.service.TaskCommandService;
import by.lebenkov.task_tracker.api.service.TaskReadService;
import by.lebenkov.task_tracker.api.validation.OnCreate;
import by.lebenkov.task_tracker.storage.dto.errorDto.ErrorResponse;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.dto.taskDto.TaskResponse;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    TaskReadService taskReadService;
    TaskCommandService taskCommandService;

    @PostMapping
    public ResponseEntity<Void> createTask(
            @RequestBody @Validated(OnCreate.class) TaskRequest taskRequest) {
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

    @PatchMapping("/{task_id}/restore")
    public ResponseEntity<Void> restoreTask(
            @PathVariable("task_id") long taskId
    ) {
        taskCommandService.restoreTask(taskId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить задачу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача найдена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{task_id}")
    public ResponseEntity<TaskResponse> fetchTaskById(
            @PathVariable("task_id") long taskId
    ) {
        return ResponseEntity.ok(taskReadService.fetchTaskById(taskId));
    }

    @GetMapping("/fetch-all-admin")
    public ResponseEntity<Page<TaskResponse>> fetchAllTasksForAdmin(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Integer priority,
            @PageableDefault(sort = "taskId", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(taskReadService.fetchAllTasksForAdmin(status, priority, pageable));
    }

    @DeleteMapping("/{task_id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("task_id") long taskId) {
        taskCommandService.deleteTaskById(taskId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{task_id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable("task_id") long taskId,
            @RequestBody @Valid TaskRequest taskRequest
    ) {
        return ResponseEntity.ok(taskCommandService.updateTask(taskId, taskRequest));
    }
}