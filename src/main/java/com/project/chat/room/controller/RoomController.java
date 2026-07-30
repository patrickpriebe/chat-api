package com.project.chat.room.controller;

import com.project.chat.room.dto.RoomRequestDTO;
import com.project.chat.room.dto.RoomResponseDTO;
import com.project.chat.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(
            @RequestBody @Valid RoomRequestDTO dto,
            Principal principal
    ) {
        RoomResponseDTO response = roomService.createRoom(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<RoomResponseDTO>> getCurrentUserRooms(Principal principal) {
        List<RoomResponseDTO> rooms = roomService.getCurrentUserRooms(principal.getName());
        return ResponseEntity.ok(rooms);
    }
}