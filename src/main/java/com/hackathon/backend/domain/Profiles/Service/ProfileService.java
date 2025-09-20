package com.hackathon.backend.domain.Profiles.Service;

import com.hackathon.backend.domain.Profiles.Dto.Request.CaregiverProfileUpdateRequest;
import com.hackathon.backend.domain.Profiles.Dto.Request.DisabledProfileUpdateRequest;
import com.hackathon.backend.domain.Profiles.Dto.Response.ProfileResponse;
import com.hackathon.backend.domain.Profiles.Entity.CaregiverProfile;
import com.hackathon.backend.domain.Profiles.Entity.DisabledProfile;
import com.hackathon.backend.domain.Profiles.Exception.ProfileErroCode;
import com.hackathon.backend.domain.Profiles.Repository.CaregiverProfileRepository;
import com.hackathon.backend.domain.Profiles.Repository.DisabledProfileRepository;
import com.hackathon.backend.domain.Users.Entity.RoleType;
import com.hackathon.backend.domain.Users.Entity.Users;
import com.hackathon.backend.domain.Users.Repository.UserRepository;
import com.hackathon.backend.global.Exception.CustomException;
import com.hackathon.backend.global.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final CaregiverProfileRepository caregiverRepo;
    private final DisabledProfileRepository disabledRepo;

    private Users loadCurrentUser(CustomUserDetails principal) {
        Long id = principal.getUser().getUserId(); // ✅ getId()로 수정
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    /** 👤 내 정보 + 역할별 프로필 합본 */
    @Transactional
    public ProfileResponse getMyProfile(CustomUserDetails principal) {
        Users user = loadCurrentUser(principal);

        ProfileResponse.ProfileResponseBuilder builder = ProfileResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .profileImgUrl(user.getProfileImgUrl())
                .role(user.getRole())
                .gender(user.getGender().name())
                .birthDate(user.getBirthDate());

        if (user.getRole() == RoleType.CAREGIVER) {
            CaregiverProfile p = Optional.ofNullable(user.getCaregiverProfile())
                    .orElseGet(() -> caregiverRepo.findById(user.getId()).orElse(null));
            if (p != null) builder.caregiver(ProfileResponse.CaregiverProfileResponse.builder()
                    .careerYears(p.getCareerYears())
                    .serviceCategories(p.getServiceCategories())
                    .regions(p.getRegions())
                    .intro(p.getIntro())
                    .build());
        } else if (user.getRole() == RoleType.DISABLED) {
            DisabledProfile p = Optional.ofNullable(user.getDisabledProfile())
                    .orElseGet(() -> disabledRepo.findById(user.getId()).orElse(null));
            if (p != null) builder.disabled(ProfileResponse.DisabledProfileResponse.builder()
                    .region(p.getRegion())
                    .registrationNumber(p.getRegistrationNumber())
                    .classification(p.getClassification())
                    .build());
        }

        return builder.build();
    }

    /** 🧑‍⚕️ Caregiver 프로필 조회(본인) → 합본 반환 */
    @Transactional
    public ProfileResponse getMyCaregiverProfile(CustomUserDetails principal) {
        Users user = loadCurrentUser(principal);
        if (user.getRole() != RoleType.CAREGIVER)
            throw new CustomException(ProfileErroCode.INVALID_ROLE);
        return getMyProfile(principal); // ✅ 합본으로 반환
    }

    /** 🧑‍⚕️ Caregiver 프로필 수정(PATCH) → 합본 반환 */
    @Transactional
    public ProfileResponse patchMyCaregiverProfile(
            CustomUserDetails principal,
            CaregiverProfileUpdateRequest req) {

        Users user = loadCurrentUser(principal);
        if (user.getRole() != RoleType.CAREGIVER)
            throw new CustomException(ProfileErroCode.INVALID_ROLE);

        CaregiverProfile p = caregiverRepo.findById(user.getId()).orElse(null);
        if (p == null) {
            p = CaregiverProfile.builder().build();
            p.linkUser(user); // @MapsId + 양방향 연결
        }

        if (req.getCareerYears() != null) p.setCareerYears(req.getCareerYears());
        if (req.getServiceCategories() != null) p.setServiceCategories(req.getServiceCategories());
        if (req.getRegions() != null) p.setRegions(req.getRegions());
        if (req.getIntro() != null) p.setIntro(req.getIntro());

        caregiverRepo.save(p);

        return getMyProfile(principal); // ✅ 저장 후 합본 반환
    }

    /** 👩‍🦽 Disabled 프로필 조회(본인) → 합본 반환 */
    @Transactional
    public ProfileResponse getMyDisabledProfile(CustomUserDetails principal) {
        Users user = loadCurrentUser(principal);
        if (user.getRole() != RoleType.DISABLED)
            throw new CustomException(ProfileErroCode.INVALID_ROLE);
        return getMyProfile(principal); // ✅ 합본으로 반환
    }

    /** 👩‍🦽 Disabled 프로필 수정(PATCH) → 합본 반환 */
    @Transactional
    public ProfileResponse patchMyDisabledProfile(
            CustomUserDetails principal,
            DisabledProfileUpdateRequest req) {

        Users user = loadCurrentUser(principal);
        if (user.getRole() != RoleType.DISABLED)
            throw new CustomException(ProfileErroCode.INVALID_ROLE);

        DisabledProfile p = disabledRepo.findById(user.getId()).orElse(null);
        if (p == null) {
            p = DisabledProfile.builder().build();
            p.linkUser(user); // @MapsId + 양방향 연결
        }

        if (req.getRegion() != null) p.setRegion(req.getRegion());
        if (req.getRegistrationNumber() != null) p.setRegistrationNumber(req.getRegistrationNumber());
        if (req.getClassification() != null) p.setClassification(req.getClassification());

        disabledRepo.save(p);

        return getMyProfile(principal); // ✅ 저장 후 합본 반환
    }
}