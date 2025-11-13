package com.alumnositm.todo.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTodoStatusValidator.class)
@Documented
public @interface ValidTodoStatus {
    
    String message() default "El estado debe ser uno de los valores válidos: PENDING, COMPLETED, IN_PROGRESS, CANCELLED";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    boolean optional() default false;
}
