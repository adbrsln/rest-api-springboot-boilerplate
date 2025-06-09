package com.restapi.demo.mapper;

import com.restapi.demo.dto.todo.TodoRequestDto;
import com.restapi.demo.dto.todo.TodoResponseDto;
import com.restapi.demo.entity.Todo;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = TodoMapper.class)
public interface TodoMapper {
    // Maps the User object within the Todo entity to the DTO fields
    @Mapping(source = "user.id", target = "userId")
//    @Mapping(source = "user.username", target = "username")
    TodoResponseDto toTodoResponseDto(Todo todo);

    // This method is used for updating an existing entity from a DTO.
    // It ignores null properties in the DTO, so you can update just one field.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTodoFromDto(TodoRequestDto dto, @MappingTarget Todo entity);
}