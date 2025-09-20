// src/main/java/com/hackathon/backend/domain/Posting/Service/PostingService.java
package com.hackathon.backend.domain.Posting.Service;

import com.hackathon.backend.domain.Posting.Dto.CreatePostingRequest;
import com.hackathon.backend.domain.Posting.Dto.PostingDetailResponse;
import com.hackathon.backend.domain.Posting.Dto.PostingMapper;
import com.hackathon.backend.domain.Posting.Entity.Posting;
import com.hackathon.backend.domain.Posting.Repository.PostingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
// 다른 import들...
import org.springframework.security.core.annotation.AuthenticationPrincipal;  // ★ 이거 추가


@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

    /** 공고 생성 */
    @Transactional
    public Long create(Long userId, CreatePostingRequest req) {
        Posting posting = PostingMapper.toEntity(userId, req);
        postingRepository.save(posting);
        return posting.getId();
    }

    /** 공고 단건 조회 (요청 포맷대로 한국어로 변환된 응답) */
    @Transactional(Transactional.TxType.SUPPORTS)
    public PostingDetailResponse get(Long id) {
        Posting p = postingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공고입니다. id=" + id));
        return PostingMapper.toResponse(p);
    }

    /** 공고 목록 조회 (페이징) */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<PostingDetailResponse> list(Pageable pageable) {
        return postingRepository.findAll(pageable)
                .map(PostingMapper::toResponse);
    }



    /** 공고 삭제 (필요 시) */
    @Transactional
    public void delete(Long id) {
        if (!postingRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 공고입니다. id=" + id);
        }
        postingRepository.deleteById(id);
    }
}
