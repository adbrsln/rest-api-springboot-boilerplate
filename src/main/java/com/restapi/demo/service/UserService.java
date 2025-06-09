package com.restapi.demo.service;


import com.restapi.demo.dto.user.UserRequestDto;
import com.restapi.demo.dto.user.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
}