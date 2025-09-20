package com.hackathon.backend.domain.Auth.Dto.Request;

import com.hackathon.backend.domain.Profiles.Entity.DisabledProfile;
import com.hackathon.backend.domain.Users.Entity.Gender;
import com.hackathon.backend.domain.Users.Entity.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {
    @NotNull(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotNull(message = "성별 정보는 필수 입력 값입니다.")
    private Gender gender;

    @NotNull(message = "생년월일은 필수 입력 값입니다.")
    private LocalDate birthDate;

    @NotNull(message = "ROLE은 CAREGIVER 또는 DISABLED 이어야 합니다.")
    private RoleType role;

    private String profileImgUrl;

    // 🔹 DISABLED 전용 입력값
    private String region;
    private String registrationNumber; // 예: 16자리면 \d{16} 검증 추천
    private Map<String, Object> classification;

}
