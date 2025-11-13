package com.alumnositm.todo.dtos.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alumnositm.todo.helpers.TodoStatus;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DisplayName("Tests de validación para UpdateTodoRequest")
public class UpdateTodoRequestTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Constructor y getters/setters funcionan correctamente")
    void testConstructorAndGetterSetter() {
        String title = "Titulo actualizado";
        String descripcion = "Descripcion actualizada";
        TodoStatus status = TodoStatus.COMPLETED;
        
        UpdateTodoRequest updateRequest = new UpdateTodoRequest(title, descripcion, status);
        
        assertEquals(title, updateRequest.getTitle());
        assertEquals(descripcion, updateRequest.getDescription());
        assertEquals(status, updateRequest.getStatus());
    }

    @Test
    @DisplayName("Setters modifican correctamente los valores")
    void testSettersModifyValues() {
        String titleOriginal = "Titulo original";
        String descOriginal = "Descripcion original";
        TodoStatus statusOriginal = TodoStatus.PENDING;
        
        UpdateTodoRequest updateRequest = new UpdateTodoRequest(titleOriginal, descOriginal, statusOriginal);
        
        String titleNuevo = "Titulo modificado";
        String descNuevo = "Descripcion modificada";
        TodoStatus statusNuevo = TodoStatus.IN_PROGRESS;
        
        updateRequest.setTitle(titleNuevo);
        updateRequest.setDescription(descNuevo);
        updateRequest.setStatus(statusNuevo);
        
        assertEquals(titleNuevo, updateRequest.getTitle());
        assertEquals(descNuevo, updateRequest.getDescription());
        assertEquals(statusNuevo, updateRequest.getStatus());
        
        assertNotEquals(titleOriginal, updateRequest.getTitle());
        assertNotEquals(descOriginal, updateRequest.getDescription());
        assertNotEquals(statusOriginal, updateRequest.getStatus());
    }

    // ==================== VALIDACIONES DE TITLE ====================

    @Test
    @DisplayName("Title null debe fallar validación")
    void testTitleNull() {
        UpdateTodoRequest request = new UpdateTodoRequest(null, "descripcion valida", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("title") && 
            v.getMessage().equals("El titulo no puede estar vacio")
        ));
    }

    @Test
    @DisplayName("Title vacío debe fallar validación")
    void testTitleEmpty() {
        UpdateTodoRequest request = new UpdateTodoRequest("", "descripcion valida", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("title") && 
            v.getMessage().equals("El titulo no puede estar vacio")
        ));
    }

    @Test
    @DisplayName("Title solo con espacios debe fallar validación")
    void testTitleOnlySpaces() {
        UpdateTodoRequest request = new UpdateTodoRequest("     ", "descripcion valida", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("title") && 
            v.getMessage().equals("El titulo no puede estar vacio")
        ));
    }

    @Test
    @DisplayName("Title con caracteres inválidos (números) debe fallar regex")
    void testTitleInvalidCharacters() {
        UpdateTodoRequest request = new UpdateTodoRequest("Titulo123", "descripcion valida", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("title") && 
            v.getMessage().contains("espacios al inicio o al final")
        ));
    }

    @Test
    @DisplayName("Title válido con acentos y ñ pasa validación")
    void testTitleValidWithAccents() {
        UpdateTodoRequest request = new UpdateTodoRequest("Título válido ñoño", "descripcion valida", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    // ==================== VALIDACIONES DE DESCRIPTION ====================

    @Test
    @DisplayName("Description null debe fallar validación")
    void testDescriptionNull() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", null, TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("description") && 
            v.getMessage().equals("La descripcion no puede estar vacia")
        ));
    }

    @Test
    @DisplayName("Description vacía debe fallar validación")
    void testDescriptionEmpty() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("description") && 
            v.getMessage().equals("La descripcion no puede estar vacia")
        ));
    }

    @Test
    @DisplayName("Description solo con espacios debe fallar validación")
    void testDescriptionOnlySpaces() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "       ", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("description") && 
            v.getMessage().equals("La descripcion no puede estar vacia")
        ));
    }

    @Test
    @DisplayName("Description con caracteres inválidos debe fallar regex")
    void testDescriptionInvalidCharacters() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "descripcion123", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("description") && 
            v.getMessage().contains("espacios al inicio o al final")
        ));
    }

    @Test
    @DisplayName("Description válida con acentos pasa validación")
    void testDescriptionValidWithAccents() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "Descripción válida española", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    // ==================== VALIDACIONES DE STATUS ====================

    @Test
    @DisplayName("Status null debe fallar validación (es obligatorio)")
    void testStatusNull() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "descripcion valida", null);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().anyMatch(v -> 
            v.getPropertyPath().toString().equals("status")
        ), "Status es obligatorio en UpdateTodoRequest");
    }

    @Test
    @DisplayName("Status PENDING es válido")
    void testStatusPending() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "descripcion valida", TodoStatus.PENDING);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("status")),
                "Status PENDING debe ser válido");
    }

    @Test
    @DisplayName("Status COMPLETED es válido")
    void testStatusCompleted() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "descripcion valida", TodoStatus.COMPLETED);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("status")),
                "Status COMPLETED debe ser válido");
    }

    @Test
    @DisplayName("Status IN_PROGRESS es válido")
    void testStatusInProgress() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "descripcion valida", TodoStatus.IN_PROGRESS);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("status")),
                "Status IN_PROGRESS debe ser válido");
    }

    @Test
    @DisplayName("Status CANCELLED es válido")
    void testStatusCancelled() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "descripcion valida", TodoStatus.CANCELLED);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("status")),
                "Status CANCELLED debe ser válido");
    }

    @Test
    @DisplayName("Todos los valores del enum TodoStatus son válidos")
    void testAllStatusValuesAreValid() {
        TodoStatus[] allStatuses = {TodoStatus.PENDING, TodoStatus.COMPLETED, TodoStatus.IN_PROGRESS, TodoStatus.CANCELLED};
        
        for (TodoStatus status : allStatuses) {
            UpdateTodoRequest request = new UpdateTodoRequest("titulo valido", "descripcion valida", status);
            Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
            
            assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("status")),
                    "Status " + status + " debe ser válido");
        }
    }

    // ==================== VALIDACIÓN COMPLETA ====================

    @Test
    @DisplayName("Request completamente válido no debe tener violaciones")
    void testCompletelyValidRequest() {
        UpdateTodoRequest request = new UpdateTodoRequest(
            "Titulo completamente valido", 
            "Descripcion completamente valida", 
            TodoStatus.IN_PROGRESS
        );
        
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty(), 
                "Un request con todos los campos válidos no debe tener violaciones. Violaciones encontradas: " + violations);
    }

    @Test
    @DisplayName("Request con múltiples errores debe reportar todas las violaciones")
    void testMultipleValidationErrors() {
        UpdateTodoRequest request = new UpdateTodoRequest(null, "", null);
        Set<ConstraintViolation<UpdateTodoRequest>> violations = validator.validate(request);
        
        // Debe haber al menos 3 violaciones (title, description, status)
        assertTrue(violations.size() >= 3, 
                "Debe haber violaciones para title, description y status");
        
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    @DisplayName("Constructor sin argumentos funciona correctamente")
    void testNoArgsConstructor() {
        UpdateTodoRequest request = new UpdateTodoRequest();
        
        assertNull(request.getTitle());
        assertNull(request.getDescription());
        assertNull(request.getStatus());
    }

    @Test
    @DisplayName("Status puede ser modificado después de la creación")
    void testStatusCanBeModifiedAfterCreation() {
        UpdateTodoRequest request = new UpdateTodoRequest("titulo", "descripcion", TodoStatus.PENDING);
        
        assertEquals(TodoStatus.PENDING, request.getStatus());
        
        request.setStatus(TodoStatus.COMPLETED);
        
        assertEquals(TodoStatus.COMPLETED, request.getStatus());
    }
}
