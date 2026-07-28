package com.project.chat.room.controller;

import com.project.chat.room.dto.RoomRequestDTO;
import com.project.chat.room.dto.RoomResponseDTO;
import com.project.chat.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody @Valid RoomRequestDTO dto) {
        RoomResponseDTO response = roomService.createRoom(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomResponseDTO>> getUserRooms(@PathVariable UUID userId) {
        List<RoomResponseDTO> rooms = roomService.getUserRooms(userId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
}