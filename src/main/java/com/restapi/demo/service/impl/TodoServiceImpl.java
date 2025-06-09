package com.restapi.demo.service.impl;

import com.restapi.demo.dto.todo.TodoRequestDto;
import com.restapi.demo.dto.todo.TodoResponseDto;
import com.restapi.demo.entity.Todo;
import com.restapi.demo.entity.User;
import com.restapi.demo.exception.ResourceNotFoundException;
import com.restapi.demo.mapper.TodoMapper;
import com.restapi.demo.repository.TodoRepository;
import com.restapi.demo.repository.UserRepository;
import com.restapi.demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    @Autowired
    private final TodoRepository todoRepository;

    @Autowired
    private final UserRepository userRepository; // Needed to link a Todo to a User

    @Autowired
    private final TodoMapper todoMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "todos", key = "#pageable")
    public Page<TodoResponseDto> getAllTodos(Pageable pageable) {
        Page<Todo> todoPage = todoRepository.findAll(pageable);
        return todoPage.map(todoMapper::toTodoResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "todos", key = "#id")
    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return todoMapper.toTodoResponseDto(todo);
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", allEntries = true)
    public TodoResponseDto createTodo(TodoRequestDto todoRequestDto) {
        // Find the user who this todo will belong to
        User user = userRepository.findById(todoRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + todoRequestDto.getUserId()));

        // Create a new Todo entity and set its properties
        Todo todo = new Todo();
        todo.setTitle(todoRequestDto.getTitle());
        todo.setDescription(todoRequestDto.getDescription());
        todo.setUser(user); // Link the todo to the user

        // Save the new todo and map it to a response DTO
        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.toTodoResponseDto(savedTodo);
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", key = "#id", allEntries = true)
    public TodoResponseDto updateTodo(Long id, TodoRequestDto todoRequestDto) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        // Use the mapper to update the entity from the DTO
        // This automatically handles null checks for partial updates
        todoMapper.updateTodoFromDto(todoRequestDto, existingTodo);

        Todo updatedTodo = todoRepository.save(existingTodo);
        return todoMapper.toTodoResponseDto(updatedTodo);
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", key = "#id", allEntries = true)
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
}