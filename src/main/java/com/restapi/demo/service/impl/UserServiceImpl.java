package com.restapi.demo.service.impl;

import com.restapi.demo.dto.user.UserRequestDto;
import com.restapi.demo.dto.user.UserResponseDto;
import com.restapi.demo.entity.User;
import com.restapi.demo.exception.ResourceNotFoundException;
import com.restapi.demo.mapper.UserMapper;
import com.restapi.demo.repository.UserRepository;
import com.restapi.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder; // Uncomment for real security

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    // private final PasswordEncoder passwordEncoder; // Inject this in a real app

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Check if username or email is already taken
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = userMapper.toUser(userRequestDto);

        // --- IMPORTANT SECURITY NOTE ---
        // In a real application, you MUST hash the password before saving.
        // String hashedPassword = passwordEncoder.encode(user.getPassword());
        // user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Use the mapper to update fields, which intelligently ignores the password
        userMapper.updateUserFromDto(userRequestDto, existingUser);

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}