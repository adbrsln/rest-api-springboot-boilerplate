package com.restapi.demo.security;


import com.restapi.demo.entity.User;
import com.restapi.demo.service.JwtService;
import com.restapi.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A custom Spring Security filter that runs once per request.
 * This filter is responsible for authenticating users by validating the JWT token
 * present in the 'Authorization' header of incoming requests.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userDetailsService;

    /**
     * The main logic of the filter. It intercepts the request, checks for a JWT,
     * validates it, and sets the user authentication in the SecurityContext.
     *
     * @param request The incoming HTTP request.
     * @param response The HTTP response.
     * @param filterChain The chain of filters to pass the request along to.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check if the Authorization header is present and correctly formatted.
        // If not, pass the request to the next filter and exit.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the JWT from the "Bearer " prefix.
        jwt = authHeader.substring(7);

        // 3. Extract the user's email (or username) from the JWT.
        userEmail = jwtService.extractUsername(jwt);

        // 4. Check if we have a user email and that the user is not already authenticated.
        // The second check is important to avoid re-authenticating on every filter in the chain.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 5. Load the user details from the database using the email from the token.
            User userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Validate the token against the loaded user details.
            if (jwtService.isTokenValid(jwt,userDetails)) {
                // 7. If the token is valid, create an authentication token.
                // This is the object Spring Security uses to represent the current user.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credentials are not needed as we are using a token
                        userDetails.getAuthorities()
                );

                // 8. Set additional details about the authentication request (e.g., IP, session ID).
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 9. Update the SecurityContextHolder with the new authentication token.
                // This is the crucial step that marks the current user as authenticated.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 10. Pass the request and response along to the next filter in the chain.
        filterChain.doFilter(request, response);
    }
}