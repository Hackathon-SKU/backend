package com.hackathon.backend.domain.Profiles.Dto.Response;

import com.hackathon.backend.domain.Users.Entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private Long userId;
    private String name;
    private String profileImgUrl;
    private RoleType role;
    private String gender;
    private LocalDate birthDate;

    private CaregiverProfileResponse caregiver; // role == CAREGIVER일 때만
    private DisabledProfileResponse disabled;   // role == DISABLED일 때만

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CaregiverProfileResponse {
        private Short careerYears;
        private Map<String, Object> serviceCategories;
        private Map<String, Object> regions;
        private String intro;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DisabledProfileResponse {
        private String region;
        private String registrationNumber;
        private Map<String, Object> classification;
    }


}
