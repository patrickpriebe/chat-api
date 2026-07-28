package com.project.chat.room.dto;

import com.project.chat.room.enums.RoomType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record RoomRequestDTO(
        String name,

        @NotNull(message = "Room type is required")
        RoomType type,

        @NotEmpty(message = "A room must have at least one member")
        Set<UUID> memberIds
) {}