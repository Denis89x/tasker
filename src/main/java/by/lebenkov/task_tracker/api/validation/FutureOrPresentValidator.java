package by.lebenkov.task_tracker.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class FutureOrPresentValidator implements ConstraintValidator<FutureOrPresent, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.isAfter(LocalDateTime.now()) || value.isEqual(LocalDateTime.now());
    }
}
