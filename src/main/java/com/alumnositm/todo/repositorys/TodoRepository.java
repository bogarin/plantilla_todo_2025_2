package com.alumnositm.todo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alumnositm.todo.entities.TodoEntity;
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    
}
