package com.hackathon.backend.domain.Profiles.Dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackathon.backend.domain.Users.Entity.RoleType;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisabledInfoResponse {

    // 공통 유저 기본 정보 (camelCase)
    private Long userId;
    private String name;
    private String gender;          // "MEN"/"WOMEN"
    private LocalDate birthDate;
    private RoleType role;          // DISABLED
    private String profileImgUrl;

    // 장애인 프로필 (엔티티 컬럼명과 맞춤)
    private String region;
    private String registrationNumber;

    // JSON 칼럼 그대로 보낼 때
    private Map<String, Object> classification;   // ex) { "tags": ["지적", "자폐성"] }
}