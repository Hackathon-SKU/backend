package com.hackathon.backend.domain.Posting.Service;


import com.hackathon.backend.domain.Posting.Dto.PostingDetailDto;
import com.hackathon.backend.domain.Posting.Dto.PostingListItemDto;
import com.hackathon.backend.domain.Profiles.Repository.DisabledProfileRepository;


import com.hackathon.backend.domain.Posting.Repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class PostingQueryService {

    private final PostingRepository postingRepository;
    private final DisabledProfileRepository disabledProfileRepository;

    public Page<PostingListItemDto> getPostingList(Pageable pageable) {
        // 목록 쿼리는 기존 findList(Pageable) 그대로 사용 (constructor/인터페이스 프로젝션 OK)
        return postingRepository.findList(pageable);
    }

    public PostingDetailDto getPostingDetail(Long postingId) {
        var p = postingRepository.findWithCollectionsById(postingId)
                .orElseThrow(() -> new IllegalArgumentException("공고가 존재하지 않습니다. id=" + postingId));

        var d = disabledProfileRepository.findById(p.getUserId())
                .orElseThrow(() -> new IllegalStateException("프로필이 없습니다. userId=" + p.getUserId()));

        return new PostingDetailDto(
                p.getId(),
                p.getUserId(),
                p.getTitle(),
                p.getPeriodStart(),
                p.getDescription(),
                p.getPreferredDays(),      // Set<Integer>
                p.getTimeBands(),          // Set<Integer>
                p.getCreatedAt(),
                p.getUpdatedAt(),
                d.getRegion(),
                d.getClassification()      // Map<String,Object>
        );
    }
}

