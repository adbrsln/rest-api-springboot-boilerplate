package com.restapi.demo.config;


import com.restapi.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Defines how to fetch user details. Spring Security will use this service
     * to load user information during the authentication process.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    /**
     * This is the bean that directly solves your error.
     * It's the data access object responsible for fetching the user details
     * and encoding passwords.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 1. Tell the provider how to get user details.
        authProvider.setUserDetailsService(userDetailsService());
        // 2. Tell the provider which password encoder to use for checking passwords.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * The AuthenticationManager is needed by the authentication endpoint.
     * It orchestrates the authentication process using the configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the password encoder bean. BCrypt is the industry standard for
     * hashing passwords securely.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}