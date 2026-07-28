package com.project.chat.message.repository;

import com.project.chat.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByRoomIdOrderByTimestampAsc(UUID roomId);
}