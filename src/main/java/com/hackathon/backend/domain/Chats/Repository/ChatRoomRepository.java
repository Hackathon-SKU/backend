package com.hackathon.backend.domain.Chats.Repository;


import com.hackathon.backend.domain.Chats.Entity.ChatRoom;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByMatchId(Long matchId);

    // 내가 참여 중인 방 목록 (최신 생성순)
    @Query("""
    select r from ChatRoom r
      join com.hackathon.backend.domain.Chats.Entity.ChatParticipant p
        on p.room = r
     where p.userId = :userId
     order by r.createdAt desc
  """)
    Page<ChatRoom> findRoomsOfUser(@Param("userId") Long userId, Pageable pageable);
}
