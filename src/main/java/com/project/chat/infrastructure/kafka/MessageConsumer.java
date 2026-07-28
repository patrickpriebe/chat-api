package com.project.chat.infrastructure.kafka;

import com.project.chat.message.dto.MessageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat.messages.new", groupId = "chat-group")
    public void consumeMessage(MessageResponseDTO message) {

        String destination = "/topic/rooms/" + message.roomId();
        messagingTemplate.convertAndSend(destination, message);
    }
}