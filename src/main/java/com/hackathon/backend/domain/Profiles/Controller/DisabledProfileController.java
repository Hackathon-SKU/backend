package com.hackathon.backend.domain.Profiles.Controller;

import com.hackathon.backend.domain.Profiles.Dto.Request.DisabledProfileUpdateRequest;
import com.hackathon.backend.domain.Profiles.Dto.Response.DisabledInfoResponse;
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
@RequestMapping("/profiles/disabled")
// ❌ @PreAuthorize("hasRole('DISABLED')")  <-- 제거!
@Tag(name = "Profiles-Disabled", description = "장애인 프로필 API")
public class DisabledProfileController {

    private final ProfileService profileService;

    /** ✅ 사용자 id로 장애인 프로필 조회 (열람 허용) */
    @Operation(summary = "Disabled 프로필 조회(by userId, flat 응답)")
    @PreAuthorize("permitAll()") // 정책에 맞게 isAuthenticated()/hasAnyRole(...)로 조정 가능
    @GetMapping("/info/{userId}")
    public ResponseEntity<GlobalWebResponse<DisabledInfoResponse>> getByUserId(
            @PathVariable Long userId
    ) {
        var body = profileService.getDisabledProfileByUserId(userId);
        return ResponseEntity.ok(GlobalWebResponse.success("200", "성공", body));
    }

    /** 본인 프로필 수정만 제한 */
    @Operation(summary = "Disabled 프로필 수정(PATCH, flat 응답)")
    @PreAuthorize("hasRole('DISABLED')")
    @PatchMapping("/info")
    public ResponseEntity<GlobalWebResponse<DisabledInfoResponse>> patch(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody DisabledProfileUpdateRequest req
    ) {
        var body = profileService.patchMyDisabledProfile(principal, req);
        return ResponseEntity.ok(GlobalWebResponse.success("200", "성공", body));
    }
}
