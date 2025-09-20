package com.hackathon.backend.domain.Chats.Repository;


import java.util.List;
import java.util.Optional;

import com.hackathon.backend.domain.Chats.Entity.ChatParticipant;
import org.springframework.data.jpa.repository.*;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    Optional<ChatParticipant> findByRoomIdAndUserId(Long roomId, Long userId);

    List<ChatParticipant> findByRoomId(Long roomId);
}