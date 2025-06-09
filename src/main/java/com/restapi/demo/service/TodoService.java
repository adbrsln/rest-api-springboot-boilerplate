package com.restapi.demo.service;

import com.restapi.demo.dto.todo.TodoRequestDto;
import com.restapi.demo.dto.todo.TodoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TodoService {
    Page<TodoResponseDto> getAllTodos(Pageable pageable);
    TodoResponseDto getTodoById(Long id);
    TodoResponseDto createTodo(TodoRequestDto todoRequestDto);
    TodoResponseDto updateTodo(Long id, TodoRequestDto todoRequestDto);
    void deleteTodo(Long id);
}