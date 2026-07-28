package com.project.chat.infrastructure.websocket.dto;

import java.util.UUID;

public record TypingEventDTO(
        UUID roomId,
        String username,
        boolean isTyping
) {}