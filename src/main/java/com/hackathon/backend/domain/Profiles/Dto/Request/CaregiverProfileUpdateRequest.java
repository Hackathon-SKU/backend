package com.hackathon.backend.domain.Profiles.Dto.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaregiverProfileUpdateRequest {

    @Schema(description = "경력(년)", example = "8", minimum = "0", maximum = "255")
    @Min(0) @Max(255)
    private Integer careerYears;  // 🔁 Short → Integer (파싱 에러 방지)

    @Schema(description = "제공 가능 카테고리", example = "{\"categories\":[\"지체장애\",\"자폐성 장애\"]}")
    private Map<String, Object> serviceCategories;

    @Schema(description = "활동 지역", example = "{\"address\":[\"야탑3동\",\"야탑2동\"]}")
    private Map<String, Object> regions;

    @Schema(description = "자기소개", example = "친절한 케어가 자신있는 복지사입니다")
    private String intro;

    private String certificateImageUrl;
}
