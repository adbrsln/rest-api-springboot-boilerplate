package com.restapi.demo.service;

import com.restapi.demo.dto.auth.AuthenticationRequest;
import com.restapi.demo.dto.auth.AuthenticationResponse;
import com.restapi.demo.dto.auth.RegisterRequest;
import com.restapi.demo.entity.User;
import com.restapi.demo.repository.UserRepository;
import com.restapi.demo.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Logic for registering a new user.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // 1. Create a new User entity from the request DTO.
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 2. Hash the password!
                .role(Role.USER)
                .build();
        // 4. Save the new user to the database.
        repository.save(user);
        // 5. Generate a JWT for the new user.
        var jwtToken = jwtService.generateToken(user);
        // 6. Return the token in the response.
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    /**
     * Logic for authenticating an existing user.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. The key step: Use the AuthenticationManager to validate the credentials.
        // This will internally use your UserDetailsService and PasswordEncoder.
        // If credentials are bad, it throws an exception.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        // 2. If authentication was successful, find the user.

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + request.getUsername()));

        // 3. Generate a new JWT for the successfully authenticated user.
        var jwtToken = jwtService.generateToken(user);
        // 4. Return the token in the response.
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}