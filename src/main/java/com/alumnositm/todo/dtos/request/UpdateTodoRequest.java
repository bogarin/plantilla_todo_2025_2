package com.alumnositm.todo.dtos.request;

import com.alumnositm.todo.helpers.TodoStatus;
import com.alumnositm.todo.validators.ValidTodoStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoRequest {
    @NotBlank(message = "El titulo no puede estar vacio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s.!]+$", message = "El titulo no puede tener espacios al inicio o al final")
    private String title;

    @NotBlank(message = "La descripcion no puede estar vacia")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s.!]+$", message = "La descripcion no puede tener espacios al inicio o al final")
    private String description;

    @ValidTodoStatus(optional = false, message = "El estado debe ser PENDING, COMPLETED, IN_PROGRESS o CANCELLED")
    private TodoStatus status;
}
