package com.hackathon.backend.domain.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "유저 생성 요청")
public record CreateUserRequest( @Schema(example = "test1@example.com") @Email @NotBlank String email,
                                 @Schema(example = "테스트유저1") @NotBlank String name,
                                 @Schema(example = "password123 (개발용)") @NotBlank String password,
                                 @Schema(example = "USER") @NotBlank String role) {}
