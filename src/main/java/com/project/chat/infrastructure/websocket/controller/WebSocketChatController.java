package com.project.chat.infrastructure.websocket.controller;

import com.project.chat.infrastructure.websocket.dto.TypingEventDTO;
import com.project.chat.room.repository.RoomRepository;
import com.project.chat.user.entity.User;
import com.project.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @MessageMapping("/typing")
    public void handleTyping(@Payload TypingEventDTO event, Principal principal) {
        if (principal == null ||
                !roomRepository.existsByIdAndMembers_Email(event.roomId(), principal.getName())) {
            throw new IllegalArgumentException("User is not authorized for this room");
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        TypingEventDTO authenticatedEvent = new TypingEventDTO(
                event.roomId(),
                user.getUsername(),
                event.isTyping()
        );

        messagingTemplate.convertAndSend(
                "/topic/rooms/" + event.roomId() + "/typing",
                authenticatedEvent
        );
    }
}