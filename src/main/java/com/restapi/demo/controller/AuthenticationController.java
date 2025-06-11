package com.restapi.demo.controller;


import com.restapi.demo.dto.auth.AuthenticationRequest;
import com.restapi.demo.dto.auth.AuthenticationResponse;
import com.restapi.demo.dto.auth.RegisterRequest;
import com.restapi.demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // <-- This sets the base URL for this controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * Endpoint for user registration.
     */
    @PostMapping("/register") // <-- This defines the .../register endpoint
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        // Delegates the actual work to the AuthenticationService
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Endpoint for user authentication (login).
     */
    @PostMapping("/authenticate") // <-- This defines the .../authenticate endpoint
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        // Delegates the actual work to the AuthenticationService
        return ResponseEntity.ok(service.authenticate(request));
    }
}