package com.hackathon.backend.domain.Posting.Repository;

import com.hackathon.backend.domain.Posting.Entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    // 커스텀 쿼리 메서드 필요 시 여기에 정의
}
