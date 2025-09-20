package com.hackathon.backend.domain.Profiles.Dto.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaregiverProfileUpdateRequest {
    private Short careerYears;              // null이면 변경 안 함
    private Map<String, Object> serviceCategories; // null이면 변경 안 함
    private Map<String, Object> regions;           // null이면 변경 안 함
    private String intro;                   // null이면 변경 안 함
    private String certificateImageUrl;     // 엔티티/DDL 정리 후 사용
}
