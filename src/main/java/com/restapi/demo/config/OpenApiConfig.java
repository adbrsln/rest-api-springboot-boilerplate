package com.restapi.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI specification for the application.
     * This includes API metadata and the security scheme for JWT authentication.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // A name for the security scheme

        return new OpenAPI()
                // Define the security scheme (JWT Bearer Token)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT Bearer token")
                        )
                )
                // Add a global security requirement to all endpoints
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // Set up the general API information
                .info(new Info()
                        .title("Spring Boot Boilerplate API")
                        .description("A comprehensive REST API boilerplate built with Spring Boot.")
                        .version("v1.0.0")
                        .license(new License().name("MIT License").url("https://github.com/adbrsln/rest-api-springboot-boilerplate/blob/main/LICENSE"))
                );
    }
}