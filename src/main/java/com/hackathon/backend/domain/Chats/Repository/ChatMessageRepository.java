package com.hackathon.backend.domain.Chats.Repository;

import java.util.List;

import com.hackathon.backend.domain.Chats.Entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 커서 기반 페이지네이션: room_id = ? AND (cursor IS NULL OR id < cursor) ORDER BY id DESC LIMIT size
    @Query("""
    select m from ChatMessage m
    where m.room.id = :roomId
      and (:cursor is null or m.id < :cursor)
    order by m.id desc
  """)
    List<ChatMessage> pageByRoomDesc(@Param("roomId") Long roomId,
                                     @Param("cursor") Long cursor,
                                     Pageable pageable);

    List<ChatMessage> findTop50ByRoomIdOrderByIdDesc(Long roomId);
}