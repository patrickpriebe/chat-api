package com.project.chat.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageRequestDTO(
        @NotBlank(message = "Content cannot be empty")
        String content,

        @NotNull(message = "Room ID is required")
        UUID roomId
) {}