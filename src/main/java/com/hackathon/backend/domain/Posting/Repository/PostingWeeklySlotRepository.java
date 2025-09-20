package com.hackathon.backend.domain.Posting.Repository;

import com.hackathon.backend.domain.Posting.Entity.PostingWeeklySlots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingWeeklySlotRepository extends JpaRepository<PostingWeeklySlots, Long> {
}
