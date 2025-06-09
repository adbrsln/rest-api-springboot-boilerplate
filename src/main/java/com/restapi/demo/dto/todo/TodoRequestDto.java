package com.restapi.demo.dto.todo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoRequestDto {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    private Boolean completed;

    @NotNull(message = "User ID must be provided")
    private Long userId;
}