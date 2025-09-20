package com.hackathon.backend.domain.Profiles.Controller;

import com.hackathon.backend.domain.Profiles.Dto.Request.CaregiverProfileUpdateRequest;
import com.hackathon.backend.domain.Profiles.Dto.Response.CaregiverInfoResponse;
import com.hackathon.backend.domain.Profiles.Dto.Response.ProfileResponse;
import com.hackathon.backend.domain.Profiles.Service.ProfileService;
import com.hackathon.backend.global.Response.GlobalWebResponse;
import com.hackathon.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.global.Response.GlobalWebResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles/caregiver")
@Tag(name = "Profiles-Caregiver", description = "복지사 프로필 API")
public class CaregiverProfileController {

    private final ProfileService profileService;

    /** ✅ 복지사 프로필 조회(by userId) */
    @Operation(summary = "Caregiver 프로필 조회(by userId, flat 응답)")
    @PreAuthorize("permitAll()") // 공개 여부 정책에 맞게 조정
    @GetMapping("/info/{userId}")
    public ResponseEntity<GlobalWebResponse<CaregiverInfoResponse>> getByUserId(
            @PathVariable Long userId
    ) {
        var body = profileService.getCaregiverProfileByUserId(userId);
        return ResponseEntity.ok(GlobalWebResponse.success("200", "성공 메시지", body));
    }

    /** 내 프로필 수정(본인) */
    @Operation(summary = "Caregiver 프로필 수정(PATCH, flat 응답)")
    @PreAuthorize("hasRole('CAREGIVER')")
    @PatchMapping("/info")
    public ResponseEntity<GlobalWebResponse<CaregiverInfoResponse>> patch(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody CaregiverProfileUpdateRequest req
    ) {
        var body = profileService.patchMyCaregiverProfile(principal, req);
        return ResponseEntity.ok(GlobalWebResponse.success("200", "성공 메시지", body));
    }
}

