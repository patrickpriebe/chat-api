package com.project.chat.user.dto;

import com.project.chat.user.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        Role role,
        LocalDateTime createdAt
) {}