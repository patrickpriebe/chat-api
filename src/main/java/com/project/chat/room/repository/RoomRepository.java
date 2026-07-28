package com.project.chat.room.repository;

import com.project.chat.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("SELECT r FROM Room r JOIN r.members m WHERE m.id = :userId")
    List<Room> findAllByMemberId(@Param("userId") UUID userId);
}