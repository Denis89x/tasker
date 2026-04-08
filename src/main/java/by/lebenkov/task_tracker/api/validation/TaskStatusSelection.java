package by.lebenkov.task_tracker.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TaskStatusSelectionValidator.class)
public @interface TaskStatusSelection {
    String message() default "Для выполненных статусов и статусов в процессе выполнения должен быть дедлайн";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
