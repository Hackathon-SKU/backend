package com.hackathon.backend.domain.Auth.Dto.Response;

import com.hackathon.backend.domain.Users.Entity.RoleType;
import com.hackathon.backend.domain.Users.Entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Builder
public class JoinResponseDto {
    @NotBlank
    private final Long id;

    @NotBlank
    private final String name;

    @Email
    private final String email;

    @NotBlank
    private final RoleType role;

    private final String profileImageUrl;
}