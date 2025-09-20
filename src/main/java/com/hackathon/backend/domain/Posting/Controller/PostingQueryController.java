package com.hackathon.backend.domain.Posting.Controller;

import com.hackathon.backend.domain.Posting.Dto.PostingDetailDto;
import com.hackathon.backend.domain.Posting.Dto.PostingListItemDto;
import com.hackathon.backend.domain.Posting.Service.PostingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postings/view")
public class PostingQueryController {

    private final PostingQueryService service;

    // 목록 (페이지네이션)
    @GetMapping
    public java.util.List<PostingListItemDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Page → content만 꺼내서 반환
        return service.getPostingList(org.springframework.data.domain.PageRequest.of(page, size))
                .getContent();
    }

    // 상세
    @GetMapping("/{id}")
    public PostingDetailDto detail(@PathVariable Long id) {
        return service.getPostingDetail(id);
    }
}
