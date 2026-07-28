package com.project.chat.infrastructure.websocket.controller;

import com.project.chat.infrastructure.websocket.dto.TypingEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/typing")
    public void handleTyping(@Payload TypingEventDTO event) {
        messagingTemplate.convertAndSend("/topic/rooms/" + event.roomId() + "/typing", event);
    }
}