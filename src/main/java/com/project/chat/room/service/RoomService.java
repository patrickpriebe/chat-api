package com.project.chat.room.service;

import com.project.chat.infrastructure.exception.ResourceNotFoundException;
import com.project.chat.room.dto.RoomRequestDTO;
import com.project.chat.room.dto.RoomResponseDTO;
import com.project.chat.room.entity.Room;
import com.project.chat.room.repository.RoomRepository;
import com.project.chat.user.dto.UserResponseDTO;
import com.project.chat.user.entity.User;
import com.project.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public RoomResponseDTO createRoom(RoomRequestDTO dto, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        List<User> members = userRepository.findAllById(dto.memberIds());
        if (members.size() != dto.memberIds().size()) {
            throw new ResourceNotFoundException("One or more users provided for the room do not exist");
        }

        Set<User> roomMembers = new HashSet<>(members);
        roomMembers.add(creator);

        Room room = Room.builder()
                .name(dto.name())
                .type(dto.type())
                .members(roomMembers)
                .build();

        room = roomRepository.save(room);
        return toDTO(room);
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDTO> getCurrentUserRooms(String authenticatedEmail) {
        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        return roomRepository.findAllByMemberId(user.getId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private RoomResponseDTO toDTO(Room room) {
        Set<UserResponseDTO> memberDTOs = room.getMembers().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt()))
                .collect(Collectors.toSet());

        return new RoomResponseDTO(room.getId(), room.getName(), room.getType(), memberDTOs, room.getCreatedAt());
    }
}