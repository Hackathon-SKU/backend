package com.hackathon.backend.domain.Profiles.Controller;

import com.hackathon.backend.domain.Profiles.Dto.Request.CaregiverProfileUpdateRequest;
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
@PreAuthorize("hasRole('CAREGIVER')") // ✅ 클래스 레벨 권한
@Tag(name = "Profiles-Caregiver", description = "복지사 프로필 API")
public class CaregiverProfileController {

    private final ProfileService profileService;
    @Operation(summary = "Caregiver 프로필 조회(본인, user 정보 포함)")
    @GetMapping("/info")
    public ResponseEntity<GlobalWebResponse<ProfileResponse>> info(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        var body = profileService.getMyCaregiverProfile(principal); // 합본 반환
        return ResponseEntity.ok(success("200", "Caregiver 프로필 조회 성공", body));
    }

    @Operation(summary = "Caregiver 프로필 수정(PATCH, user 정보 포함)")
    @PatchMapping("/info")
    public ResponseEntity<GlobalWebResponse<ProfileResponse>> patch(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody CaregiverProfileUpdateRequest req
    ) {
        var body = profileService.patchMyCaregiverProfile(principal, req); // 저장 후 합본 반환
        return ResponseEntity.ok(success("200", "Caregiver 프로필 수정 성공", body));
    }
}
