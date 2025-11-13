package com.alumnositm.todo.dtos.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class CreateTodoRequestTest {

    @Test
    void testConstructorAndGetterSetter() {
        String title = " prueba de todo";
        String descripcion = "pruebas de cuerpo de la descripcion";
        CreateTodoRequest createTodoRequest = new CreateTodoRequest(title, descripcion);
        assertEquals(descripcion, createTodoRequest.getDescription());
        assertEquals(title, createTodoRequest.getTitle());
    }

    // *profe quiere que aga una instania con valores en ella
    // *el profe quere que modifique los valores
    // *el profe quiere que compare los valores modificados con la instancia
    @Test
    void testConstructorSetterModify() {
        String title = " prueba de todo";
        String descripcion = null;
        String titleModify = " prueba de todo or que dice lo mismo mi compa";
        String descripcionModify = "pruebas de cuerpo de la descripcion es que nose que poner el profe me dejo esto";

        CreateTodoRequest createTodoRequest = new CreateTodoRequest(title, descripcion);

        createTodoRequest.setTitle(titleModify);
        createTodoRequest.setDescription(descripcionModify);

        assertEquals(titleModify, createTodoRequest.getTitle());
        assertEquals(descripcionModify, createTodoRequest.getDescription());

        assertNotEquals(title, createTodoRequest.getTitle());
        assertNotEquals(descripcion, createTodoRequest.getDescription());
    }

    @Test
    void testTitleNotblankValidation() {
        Set<ConstraintViolation<CreateTodoRequest>> violations;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // Test null title
        CreateTodoRequest requestNullTitleTodo = new CreateTodoRequest(null, "descripcion valida");
        violations = validator.validate(requestNullTitleTodo);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El titulo no puede estar vacio")));

        // Test empty title
        CreateTodoRequest requestEmptyTitleTodo = new CreateTodoRequest("", "descripcion valida");
        violations = validator.validate(requestEmptyTitleTodo);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El titulo no puede estar vacio")));

        // Test space title
        CreateTodoRequest requestSpaceTitleTodo = new CreateTodoRequest("     ", "descripcion valida");
        violations = validator.validate(requestSpaceTitleTodo);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El titulo no puede estar vacio")));
    }

    @Test
    void testDescriptionNotblankValidation() {
        Set<ConstraintViolation<CreateTodoRequest>> violations;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // Test null description
        CreateTodoRequest requestNullDescriptionTodo = new CreateTodoRequest("titulo valido", null);
        violations = validator.validate(requestNullDescriptionTodo);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La descripcion no puede estar vacia")));

        // Test empty title
        CreateTodoRequest requestEmptyDescriptionTodo = new CreateTodoRequest("titulo valido", "");
        violations = validator.validate(requestEmptyDescriptionTodo);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La descripcion no puede estar vacia")));

        // Test space title
        CreateTodoRequest requestSpaceDescriptionTodo = new CreateTodoRequest("titulo valido", "       ");
        violations = validator.validate(requestSpaceDescriptionTodo);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La descripcion no puede estar vacia")));
    }

    @Test
    void testValidRegexTitleTodoRequest() {
        Set<ConstraintViolation<CreateTodoRequest>> violations;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        CreateTodoRequest requestValidTitleTodo = new CreateTodoRequest("Titulo Valido1", "descripcion valida");
        violations = validator.validate(requestValidTitleTodo);
        System.out.println(violations.isEmpty()? "cumplimos con el regex" : "no cumplimos con el regex");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El titulo no puede tener espacios al inicio o al final")));
    }
}
