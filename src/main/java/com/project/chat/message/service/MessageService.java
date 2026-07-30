package com.project.chat.message.service;

import com.project.chat.infrastructure.exception.BusinessRuleException;
import com.project.chat.infrastructure.exception.ResourceNotFoundException;
import com.project.chat.message.dto.MessageRequestDTO;
import com.project.chat.message.dto.MessageResponseDTO;
import com.project.chat.message.entity.Message;
import com.project.chat.message.enums.MessageStatus;
import com.project.chat.message.repository.MessageRepository;
import com.project.chat.room.entity.Room;
import com.project.chat.room.repository.RoomRepository;
import com.project.chat.user.entity.User;
import com.project.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final KafkaTemplate<String, MessageResponseDTO> kafkaTemplate;

    @Transactional
    public MessageResponseDTO saveMessage(MessageRequestDTO dto, String authenticatedEmail) {
        User sender = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        Room room = roomRepository.findById(dto.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        boolean isMember = room.getMembers().stream().anyMatch(member -> member.getId().equals(sender.getId()));
        if (!isMember) {
            throw new BusinessRuleException("Sender is not a member of this room");
        }

        Message message = Message.builder()
                .content(dto.content())
                .sender(sender)
                .room(room)
                .status(MessageStatus.SENT)
                .build();

        message = messageRepository.save(message);

        MessageResponseDTO response = toDTO(message);

        kafkaTemplate.send("chat.messages.new", response.roomId().toString(), response);

        return response;
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getRoomHistory(UUID roomId, String authenticatedEmail) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        boolean isMember = room.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(authenticatedEmail));

        if (!isMember) {
            throw new BusinessRuleException("User is not a member of this room");
        }

        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MessageResponseDTO toDTO(Message message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getContent(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getRoom().getId(),
                message.getStatus(),
                message.getTimestamp()
        );
    }
}