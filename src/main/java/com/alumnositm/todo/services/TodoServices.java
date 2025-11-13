package com.alumnositm.todo.services;

import java.util.List;

import com.alumnositm.todo.dtos.request.CreateTodoRequest;
import com.alumnositm.todo.dtos.request.UpdateTodoRequest;
import com.alumnositm.todo.entities.TodoEntity;

public interface TodoServices {

    List   <TodoEntity> allTodos();

    TodoEntity createTodo(CreateTodoRequest createTodoRequest);

    TodoEntity findById(int idTodo);

    TodoEntity updateTodoById(int idTodo, UpdateTodoRequest entity);

    List<TodoEntity> findTodosByTitle(String queryParam);

    boolean deleteTodoById(int idTodo);
}
