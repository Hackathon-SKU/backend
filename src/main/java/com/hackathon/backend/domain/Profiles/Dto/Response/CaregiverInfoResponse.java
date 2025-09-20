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
public class CaregiverInfoResponse {

    // ✅ 유저 기본 정보 (camelCase 유지)
    private Long userId;
    private String name;
    private String gender;      // "MEN"/"WOMEN"
    private LocalDate birthDate;
    private RoleType role;      // CAREGIVER
    private String profileImgUrl;

    // ✅ 복지사 정보 (요구사항대로 snake_case)
    @JsonProperty("career_years")
    private Short careerYears;

    @JsonProperty("service_categories")
    private Map<String, Object> serviceCategories; // ex) { "categories": ["지체장애", "자폐성 장애"] }

    private Map<String, Object> regions;           // ex) { "address": ["야탑3동", "야탑2동"] }

    private String intro;
}