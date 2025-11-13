package com.alumnositm.todo.services.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.alumnositm.todo.dtos.request.CreateTodoRequest;
import com.alumnositm.todo.dtos.request.UpdateTodoRequest;
import com.alumnositm.todo.entities.TodoEntity;
import com.alumnositm.todo.helpers.TodoStatus;
import com.alumnositm.todo.repositorys.TodoRepository;
import com.alumnositm.todo.services.TodoServices;

@Service
public class TodoServicesImpl implements TodoServices {

    private final TodoRepository todoRepository;
    private final JdbcTemplate jdbcTemplate;

    public TodoServicesImpl(TodoRepository todoRepository, JdbcTemplate jdbcTemplate) {
        this.todoRepository = todoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TodoEntity> allTodos() {
        // List<TodoEntity> todos = List.of(
        //     new TodoEntity(1L, "Learn Spring Boot", "Complete the Spring Boot tutorial", TodoStatus.PENDING),
        //     new TodoEntity(2L, "Build a REST API", "Create a RESTful API using Spring Boot", TodoStatus.IN_PROGRESS),
        //     new TodoEntity(3L, "Write Unit Tests", "Write unit tests for the application", TodoStatus.COMPLETED)
        // );
       List<TodoEntity> todos  = todoRepository.findAll();
        return todos;
    }

    @Override
    public TodoEntity createTodo(CreateTodoRequest createTodoRequest) {
    //    TodoEntity entity= TodoEntity.builder()
    //    .title(createTodoRequest.getTitle())
    //    .description(createTodoRequest.getDescription())
    //    .status(TodoStatus.PENDING)
    //    .build();

    TodoEntity entity = new TodoEntity();
    entity.setTitle(createTodoRequest.getTitle());
    entity.setDescription(createTodoRequest.getDescription());
    entity.setStatus(TodoStatus.PENDING);

       return todoRepository.save(entity);
    }

    @Override
    public TodoEntity findById(int idTodo) {
       TodoEntity todo = todoRepository.findById((long)idTodo).orElse(null);
       return todo;
    }

    @Override
    public TodoEntity updateTodoById(int idTodo, UpdateTodoRequest entity) {
        TodoEntity todoEntity = todoRepository.findById((long)idTodo).orElse(null);
        if(todoEntity!=null){
            todoEntity.setTitle(entity.getTitle());
            todoEntity.setDescription(entity.getDescription());
            // el error viene desde aqui
            todoEntity.setStatus(TodoStatus.COMPLETED);
            todoRepository.save(todoEntity);
            return todoEntity;
        }
        return null;

    }

    @Override
    public List<TodoEntity> findTodosByTitle(String queryParam) {
        String sql = "Select * From todos where title like '%"+queryParam+"%'";
        RowMapper<TodoEntity> rowMapper = (rs,rowNum)->{
            TodoEntity todo = new TodoEntity();
            todo.setId(rs.getLong("id"));
            todo.setTitle(rs.getString("title"));
            todo.setDescription(rs.getString("description"));
            todo.setStatus(TodoStatus.valueOf(rs.getString("status")));
            return todo;
        };

        List<TodoEntity> todos = jdbcTemplate.query(sql, rowMapper);
        return todos;
    }

    @Override
    public boolean deleteTodoById(int idTodo) {
          TodoEntity todoEntity = todoRepository.findById((long)idTodo).orElse(null);
        if(todoEntity!=null){
            todoRepository.delete(todoEntity);
            return true;
        }
        return false;
    }

    



}
