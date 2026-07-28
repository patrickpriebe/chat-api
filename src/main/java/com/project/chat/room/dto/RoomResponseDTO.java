package com.project.chat.room.dto;

import com.project.chat.room.enums.RoomType;
import com.project.chat.user.dto.UserResponseDTO;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record RoomResponseDTO(
        UUID id,
        String name,
        RoomType type,
        Set<UserResponseDTO> members,
        LocalDateTime createdAt
) {}