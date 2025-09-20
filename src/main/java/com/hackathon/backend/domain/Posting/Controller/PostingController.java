package com.hackathon.backend.domain.Posting.Controller;

import com.hackathon.backend.domain.Posting.Dto.CreatePostingRequest;
import com.hackathon.backend.domain.Posting.Dto.PostingResponse;
import com.hackathon.backend.domain.Posting.Service.PostingService;
import com.hackathon.backend.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/postings") // 리소스 복수형
@RequiredArgsConstructor
@Validated
public class PostingController {

    private final PostingService postingService;

    /** 공고 생성 */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails principal, // JWT 필터가 넣어준 principal
            @Valid @RequestBody CreatePostingRequest req
    ) {
        Long userId = principal.getUser().getUserId();   // 현재 구조대로 userId 추출
        Long id = postingService.create(userId, req);

        return ResponseEntity
                .created(URI.create("/api/postings/" + id)) // ← Location 헤더 올바르게
                .body(Map.of(
                        "isSuccess", true,
                        "code", "POSTING_CREATED",
                        "message", "공고가 생성되었습니다.",
                        "result", Map.of("id", id)
                ));
    }

    /** 공고 단건 조회 */
    @GetMapping(value = "/{id}", produces = "application/json")
    public PostingResponse get(@PathVariable Long id) {
        return postingService.get(id);
    }
}
