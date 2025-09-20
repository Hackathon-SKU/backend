package com.hackathon.backend.domain.Users.Entity;


import com.hackathon.backend.domain.Auth.Dto.Request.JoinRequestDto;
import com.hackathon.backend.domain.Profiles.Entity.CaregiverProfile;
import com.hackathon.backend.domain.Profiles.Entity.DisabledProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_role", columnList = "role"),
                @Index(name = "idx_users_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false, updatable = false)
    private Long id;

    @Column(name="email", nullable=false, length=255)
    private String email;

    @Column(name="password_hash", nullable=false)
    private String passwordHash;

    @Column(name="name", nullable=false, length=100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="gender", nullable=false, length=12)
    private Gender gender;

    @Column(name="birth_date", nullable=false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable=false, length=20)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false, length=12)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name="profile_img_url", length=512)
    private String profileImgUrl;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    // 🔗 1:1 프로필 (선택적). 프로필 테이블에서 PK=FK(Shared PK)로 매핑됨.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DisabledProfile disabledProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CaregiverProfile caregiverProfile;

    // 편의 메서드 (양방향 관계 세팅) 👍
    public void attachServiceUserProfile(DisabledProfile profile) {
        this.disabledProfile = profile;
        if (profile != null) profile.setUser(this);
    }

    public void attachCaregiverProfile(CaregiverProfile profile) {
        this.caregiverProfile = profile;
        if (profile != null) profile.setUser(this);
    }


    public static Users from(JoinRequestDto dto) {
        return Users.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword()) // DTO는 password, 엔티티는 passwordHash
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .role(dto.getRole())
                .status(UserStatus.ACTIVE) // 기본값
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
