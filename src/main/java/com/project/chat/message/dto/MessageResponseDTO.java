package com.project.chat.message.dto;

import com.project.chat.message.enums.MessageStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDTO(
        UUID id,
        String content,
        UUID senderId,
        String senderUsername,
        UUID roomId,
        MessageStatus status,
        LocalDateTime timestamp
) {}