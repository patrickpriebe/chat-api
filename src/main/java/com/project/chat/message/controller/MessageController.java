package com.project.chat.message.controller;

import com.project.chat.message.dto.MessageRequestDTO;
import com.project.chat.message.dto.MessageResponseDTO;
import com.project.chat.message.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @RequestBody @Valid MessageRequestDTO dto,
            Principal principal
    ) {
        MessageResponseDTO response = messageService.saveMessage(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<MessageResponseDTO>> getRoomHistory(
            @PathVariable UUID roomId,
            Principal principal
    ) {
        List<MessageResponseDTO> history = messageService.getRoomHistory(roomId, principal.getName());
        return ResponseEntity.ok(history);
    }
}