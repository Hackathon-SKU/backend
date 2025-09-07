package com.hackathon.backend.global.security;

import com.hackathon.backend.global.Jwt.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserInfoDto {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private RoleType role;
    private String profileImageUrl;
}
