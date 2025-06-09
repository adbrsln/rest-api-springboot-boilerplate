package com.restapi.demo.controller;

import com.restapi.demo.dto.PaginatedResponse;
import com.restapi.demo.dto.todo.TodoRequestDto;
import com.restapi.demo.dto.todo.TodoResponseDto;
import com.restapi.demo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Tag(name = "Todo Management", description = "APIs for managing Todos")
public class TodoController {

    @Autowired
    private final TodoService todoService;

    @Operation(summary = "Get all todos")
    @GetMapping
    public ResponseEntity<PaginatedResponse<TodoResponseDto>> getAllTodos(
            @ParameterObject // This tells Swagger to render the pageable parameters correctly
            @PageableDefault(size = 10) // Optional: Set default values
            Pageable pageable,
            HttpServletRequest request
    ) {
        Page<TodoResponseDto> todoPage = todoService.getAllTodos(pageable);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        PaginatedResponse<TodoResponseDto> response = new PaginatedResponse<>(todoPage, uriBuilder);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a todo by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @Operation(summary = "Create a new todo")
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(@Valid @RequestBody TodoRequestDto todoRequestDto) {
        TodoResponseDto createdTodo = todoService.createTodo(todoRequestDto);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing todo")
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequestDto todoRequestDto) {
        return ResponseEntity.ok(todoService.updateTodo(id, todoRequestDto));
    }

    @Operation(summary = "Delete a todo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}