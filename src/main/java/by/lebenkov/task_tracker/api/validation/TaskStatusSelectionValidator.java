package by.lebenkov.task_tracker.api.validation;

import by.lebenkov.task_tracker.storage.dto.taskDto.TaskRequest;
import by.lebenkov.task_tracker.storage.enums.TaskStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TaskStatusSelectionValidator implements ConstraintValidator<TaskStatusSelection, TaskRequest> {

    @Override
    public boolean isValid(TaskRequest value, ConstraintValidatorContext context) {
        if (value.getTaskStatus() != TaskStatus.NEW_TASK && value.getDueDate() == null) {
            return false;
        }
        return true;
    }
}
