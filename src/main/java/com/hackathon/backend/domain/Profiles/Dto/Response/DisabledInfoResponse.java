package com.hackathon.backend.domain.Profiles.Dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackathon.backend.domain.Users.Entity.RoleType;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisabledInfoResponse {

    // ✅ 유저 기본 정보
    private Long userId;
    private String name;
    private String gender;      // "MEN"/"WOMEN"
    private LocalDate birthDate;
    private RoleType role;      // DISABLED
    private String profileImgUrl;

    // ✅ 장애인 프로필 (flat)
    private String region;

    @JsonProperty("registration_number")
    private String registrationNumber;

    private Map<String, Object> classification; // ex) { ... } (빈 맵 기본)
}