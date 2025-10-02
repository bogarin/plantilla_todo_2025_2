package com.alumnositm.todo.dtos.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CreateTodoRequestTest {

    @Test
    void testConstructorAndGetterSetter(){
        String title = " prueba de todo";
        String descripcion = "pruebas de cuerpo de la descripcion";
String descripcionError = " pruebas de cuerpo de la descripcion ";

        CreateTodoRequest createTodoRequest = new CreateTodoRequest(title, descripcion);
        assertEquals(descripcionError, createTodoRequest.getDescription());
        assertEquals(title, createTodoRequest.getTitle());
        
    }
}
