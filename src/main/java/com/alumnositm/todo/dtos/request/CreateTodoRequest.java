package com.alumnositm.todo.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateTodoRequest {
    @NotBlank(message = "El titulo no puede estar vacio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s.!]+$", message = "El titulo no puede tener espacios al inicio o al final")
    private String title;

    @NotBlank(message = "La descripcion no puede estar vacia")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s.!]+$", message = "La descripcion no puede tener espacios al inicio o al final")
    private String description;

    public CreateTodoRequest( String title, String description) {
        this.title = title;
        this.description = description;
    }
}
