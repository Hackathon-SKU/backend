package com.hackathon.backend.domain.Posting.Repository;

import com.hackathon.backend.domain.Posting.Dto.PostingListItemDto;
import com.hackathon.backend.domain.Posting.Entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long> {

    @EntityGraph(attributePaths = {"preferredDays", "timeBands"})
    Optional<Posting> findById(Long id);

    @Query("""
        SELECT new com.hackathon.backend.domain.Posting.Dto.PostingListItemDto(
            p.id, p.title, p.userId, d.region, d.classification
        )
        FROM Posting p
        JOIN DisabledProfile d ON d.userId = p.userId
        ORDER BY p.createdAt DESC
    """)
    Page<PostingListItemDto> findList(Pageable pageable);

    // ✅ 대체: 엔티티 로드용
    @EntityGraph(attributePaths = {"preferredDays", "timeBands"})
    Optional<Posting> findWithCollectionsById(Long id);

}

