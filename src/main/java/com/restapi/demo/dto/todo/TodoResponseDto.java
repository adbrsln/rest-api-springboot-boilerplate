package com.restapi.demo.dto.todo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_completed")
    private boolean completed;

    @JsonProperty("user_id")
    private Long userId; // Include the user's ID

    @JsonProperty("username")
    private String username; // Also include the username for convenience

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
