package com.hackathon.backend.domain.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "삭제 요청 - 이메일 기반")
public record DeleteByEmailRequest(@Schema(example = "test1@example.com") @Email @NotBlank String email) {
}
