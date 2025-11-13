package com.alumnositm.todo.validators;

import com.alumnositm.todo.helpers.TodoStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTodoStatusValidator implements ConstraintValidator<ValidTodoStatus, TodoStatus> {

    private boolean optional;

    @Override
    public void initialize(ValidTodoStatus constraintAnnotation) {
        this.optional = constraintAnnotation.optional();
    }

    @Override
    public boolean isValid(TodoStatus value, ConstraintValidatorContext context) {
        // Si es opcional y el valor es null, es válido
        if (optional && value == null) {
            return true;
        }
        
        // Si no es opcional y el valor es null, es inválido
        if (!optional && value == null) {
            return false;
        }
        
        // Verificar que el valor sea uno de los valores válidos del enum
        try {
            return value == TodoStatus.PENDING || 
                   value == TodoStatus.COMPLETED || 
                   value == TodoStatus.IN_PROGRESS || 
                   value == TodoStatus.CANCELLED;
        } catch (Exception e) {
            return false;
        }
    }
}
