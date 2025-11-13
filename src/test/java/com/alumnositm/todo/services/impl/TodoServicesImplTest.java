package com.alumnositm.todo.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alumnositm.todo.dtos.request.CreateTodoRequest;
import com.alumnositm.todo.dtos.request.UpdateTodoRequest;
import com.alumnositm.todo.entities.TodoEntity;
import com.alumnositm.todo.helpers.TodoStatus;
import com.alumnositm.todo.repositorys.TodoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio TodoServicesImpl")
class TodoServicesImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServicesImpl todoServices;

    private TodoEntity sampleTodo;
    private CreateTodoRequest createRequest;

    @BeforeEach
    void setUp() {
        sampleTodo = new TodoEntity();
        sampleTodo.setId(1L);
        sampleTodo.setTitle("Sample Todo");
        sampleTodo.setDescription("Sample Description");
        sampleTodo.setStatus(TodoStatus.PENDING);

        createRequest = new CreateTodoRequest("New Todo", "New Description");
    }

    @Test
    @DisplayName("allTodos() debe retornar lista de todos del repositorio")
    void allTodos_shouldReturnAllTodos() {
        // Given
        TodoEntity todo1 = new TodoEntity();
        todo1.setId(1L);
        todo1.setTitle("Todo 1");
        todo1.setStatus(TodoStatus.PENDING);

        TodoEntity todo2 = new TodoEntity();
        todo2.setId(2L);
        todo2.setTitle("Todo 2");
        todo2.setStatus(TodoStatus.COMPLETED);

        List<TodoEntity> expectedTodos = List.of(todo1, todo2);
        given(todoRepository.findAll()).willReturn(expectedTodos);

        // When
        List<TodoEntity> result = todoServices.allTodos();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Todo 1", result.get(0).getTitle());
        assertEquals("Todo 2", result.get(1).getTitle());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("allTodos() debe retornar lista vacía cuando no hay todos")
    void allTodos_shouldReturnEmptyListWhenNoTodos() {
        // Given
        given(todoRepository.findAll()).willReturn(List.of());

        // When
        List<TodoEntity> result = todoServices.allTodos();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("createTodo() debe crear y guardar un nuevo todo con estado PENDING")
    void createTodo_shouldCreateAndSaveTodoWithPendingStatus() {
        // Given
        TodoEntity savedTodo = new TodoEntity();
        savedTodo.setId(1L);
        savedTodo.setTitle(createRequest.getTitle());
        savedTodo.setDescription(createRequest.getDescription());
        savedTodo.setStatus(TodoStatus.PENDING);

        given(todoRepository.save(any(TodoEntity.class))).willReturn(savedTodo);

        // When
        TodoEntity result = todoServices.createTodo(createRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Todo", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(TodoStatus.PENDING, result.getStatus());
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    @DisplayName("createTodo() debe invocar save del repositorio con datos correctos")
    void createTodo_shouldInvokeSaveWithCorrectData() {
        // Given
        given(todoRepository.save(any(TodoEntity.class))).willReturn(sampleTodo);

        // When
        todoServices.createTodo(createRequest);

        // Then
        verify(todoRepository).save(argThat(entity -> 
            entity.getTitle().equals("New Todo") &&
            entity.getDescription().equals("New Description") &&
            entity.getStatus() == TodoStatus.PENDING
        ));
    }

    @Test
    @DisplayName("findById() debe retornar todo cuando existe")
    void findById_shouldReturnTodoWhenExists() {
        // Given
        given(todoRepository.findById(1L)).willReturn(Optional.of(sampleTodo));

        // When
        TodoEntity result = todoServices.findById(1);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sample Todo", result.getTitle());
        assertEquals(TodoStatus.PENDING, result.getStatus());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById() debe retornar null cuando no existe")
    void findById_shouldReturnNullWhenNotExists() {
        // Given
        given(todoRepository.findById(999L)).willReturn(Optional.empty());

        // When
        TodoEntity result = todoServices.findById(999);

        // Then
        assertNull(result);
        verify(todoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("updateTodoById() debe actualizar y retornar todo cuando existe")
    void updateTodoById_shouldUpdateAndReturnTodoWhenExists() {
        // Given
        UpdateTodoRequest updateRequest = new UpdateTodoRequest("Updated Title", "Updated Description", TodoStatus.COMPLETED);
        given(todoRepository.findById(1L)).willReturn(Optional.of(sampleTodo));
        given(todoRepository.save(any(TodoEntity.class))).willReturn(sampleTodo);

        // When
        TodoEntity result = todoServices.updateTodoById(1, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TodoStatus.COMPLETED, result.getStatus());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(sampleTodo);
    }

    @Test
    @DisplayName("updateTodoById() debe cambiar status a COMPLETED al actualizar")
    void updateTodoById_shouldChangeStatusToCompleted() {
        // Given
        UpdateTodoRequest updateRequest = new UpdateTodoRequest("Updated", "Updated Desc", TodoStatus.COMPLETED);
        given(todoRepository.findById(1L)).willReturn(Optional.of(sampleTodo));
        given(todoRepository.save(any(TodoEntity.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        TodoEntity result = todoServices.updateTodoById(1, updateRequest);

        // Then
        assertEquals(TodoStatus.COMPLETED, result.getStatus());
        verify(todoRepository).save(argThat(entity -> entity.getStatus() == TodoStatus.COMPLETED));
    }

    @Test
    @DisplayName("updateTodoById() debe retornar null cuando no existe")
    void updateTodoById_shouldReturnNullWhenNotExists() {
        // Given
        UpdateTodoRequest updateRequest = new UpdateTodoRequest("Updated", "Updated Desc", TodoStatus.IN_PROGRESS);
        given(todoRepository.findById(999L)).willReturn(Optional.empty());

        // When
        TodoEntity result = todoServices.updateTodoById(999, updateRequest);

        // Then
        assertNull(result);
        verify(todoRepository, times(1)).findById(999L);
        verify(todoRepository, never()).save(any(TodoEntity.class));
    }


}
