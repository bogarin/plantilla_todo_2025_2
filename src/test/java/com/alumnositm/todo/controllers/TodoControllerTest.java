package com.alumnositm.todo.controllers;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.alumnositm.todo.dtos.request.CreateTodoRequest;
import com.alumnositm.todo.dtos.request.UpdateTodoRequest;
import com.alumnositm.todo.entities.TodoEntity;
import com.alumnositm.todo.helpers.TodoStatus;
import com.alumnositm.todo.services.TodoServices;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;

    @Mock
    private TodoServices todoServices;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setUp() {
        // Inicializar MockMvc con el controlador y los mocks inyectados
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
        // Inicializar ObjectMapper para serializar JSON
        objectMapper = new ObjectMapper();
    }

    private TodoEntity sampleTodo(Long id) {
        TodoEntity t = new TodoEntity();
        t.setId(id);
        t.setTitle("New Todo");
        t.setDescription("Description of the new todo");
        t.setStatus(TodoStatus.PENDING);
        return t;
    }

    @Test
    @DisplayName("GET /api/v1/todos -> 200 y lista")
    void allTodos_ok() throws Exception {
        given(todoServices.allTodos()).willReturn(List.of(sampleTodo(1L), sampleTodo(2L)));

        mockMvc.perform(get("/api/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("New Todo"));
    }

    @Test
    @DisplayName("GET /api/v1/todos/{id} encontrado -> 200")
    void findById_found() throws Exception {
        given(todoServices.findById(1)).willReturn(sampleTodo(1L));

        mockMvc.perform(get("/api/v1/todos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /api/v1/todos/{id} no encontrado -> 404")
    void findById_notFound() throws Exception {
        given(todoServices.findById(999)).willReturn(null);

        mockMvc.perform(get("/api/v1/todos/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/todos -> 201 Created y Location")
    void createTodo_created() throws Exception {
        CreateTodoRequest req = new CreateTodoRequest("New Todo", "Description of the new todo");
        TodoEntity saved = sampleTodo(1L);
        given(todoServices.createTodo(any(CreateTodoRequest.class))).willReturn(saved);

        mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/v1/todos/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Todo"));
    }

    @Test
    @DisplayName("PUT /api/v1/todos/update/{id} encontrado -> 200")
    void update_found() throws Exception {
        UpdateTodoRequest req = new UpdateTodoRequest("Updated", "Desc updated", TodoStatus.COMPLETED);
        TodoEntity updated = sampleTodo(1L);
        updated.setTitle("Updated");
        updated.setDescription("Desc updated");
        updated.setStatus(TodoStatus.COMPLETED);

        given(todoServices.updateTodoById(eq(1), any(UpdateTodoRequest.class))).willReturn(updated);

        mockMvc.perform(put("/api/v1/todos/update/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("PUT /api/v1/todos/update/{id} no encontrado -> 404")
    void update_notFound() throws Exception {
        UpdateTodoRequest req = new UpdateTodoRequest("Updated", "Desc updated", TodoStatus.IN_PROGRESS);
        given(todoServices.updateTodoById(eq(999), any(UpdateTodoRequest.class))).willReturn(null);

        mockMvc.perform(put("/api/v1/todos/update/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/todos/search?q= -> 200 y lista")
    void search_ok() throws Exception {
        given(todoServices.findTodosByTitle("New")).willReturn(List.of(sampleTodo(1L)));

        mockMvc.perform(get("/api/v1/todos/search").param("q", "New"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("New Todo"));
    }
}
