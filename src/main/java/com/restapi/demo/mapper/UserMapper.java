package com.restapi.demo.mapper;

import com.restapi.demo.dto.user.UserRequestDto;
import com.restapi.demo.dto.user.UserResponseDto;
import com.restapi.demo.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserMapper {

    // Maps a request DTO to a User entity for creation.
    User toUser(UserRequestDto requestDto);

    // Maps a User entity to a response DTO. The password is automatically excluded.
    UserResponseDto toUserResponseDto(User user);

    // Updates an existing User entity from a DTO.
    // Ignores null fields in the DTO, allowing for partial updates.
    // CRITICALLY, it ignores the password field to prevent accidental changes via this method.
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserRequestDto dto, @MappingTarget User entity);
}