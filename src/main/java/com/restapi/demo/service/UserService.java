package com.restapi.demo.service;


import com.restapi.demo.dto.user.UserRequestDto;
import com.restapi.demo.dto.user.UserResponseDto;
import com.restapi.demo.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}