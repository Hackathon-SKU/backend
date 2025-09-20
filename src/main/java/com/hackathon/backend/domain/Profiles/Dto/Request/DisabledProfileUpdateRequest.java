package com.hackathon.backend.domain.Profiles.Dto.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisabledProfileUpdateRequest {
    private String region;
    private String registrationNumber;
    private Map<String, Object> classification;
}
