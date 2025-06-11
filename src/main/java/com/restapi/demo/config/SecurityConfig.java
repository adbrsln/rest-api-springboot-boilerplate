package com.restapi.demo.config;

import com.restapi.demo.security.CustomAccessDeniedHandler;
import com.restapi.demo.security.DelegatedAuthenticationEntryPoint;
import com.restapi.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables method-level security like @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private final AuthenticationProvider authenticationProvider;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    private static final String[] WHITE_LIST_URLS = {
            "/auth/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    /**
     * Defines the security filter chain that applies to all HTTP requests.
     * This is the modern replacement for WebSecurityConfigurerAdapter.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection for stateless REST APIs.
                .csrf(AbstractHttpConfigurer::disable)

                // Define authorization rules for HTTP requests.
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URLS).permitAll() // Permit all requests to white-listed URLs
                                // .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Example for role-specific endpoints
                                .anyRequest().authenticated() // All other requests must be authenticated
                )

                // Configure session management to be stateless.
                // Spring Security will not create or use any HttpSession.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set the custom authentication provider.
                .authenticationProvider(authenticationProvider)

                // Add the custom JWT filter before the standard UsernamePasswordAuthenticationFilter.
                // This ensures our JWT logic runs on every request.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // This is the updated, robust exception handling configuration
                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(delegatedAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                );

        return http.build();
    }
}