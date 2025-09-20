package com.hackathon.backend.domain.Profiles.Entity;

import com.hackathon.backend.domain.Users.Entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "caregiver_profiles",
        indexes = {
                @Index(name = "idx_caregiver_profiles_career_years", columnList = "career_years")
        }
)
public class CaregiverProfile {

    /** 공유 PK = FK */
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** Users <-> CaregiverProfile : 1:1, Shared PK */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    /** MySQL TINYINT UNSIGNED */
    @Column(name = "career_years", columnDefinition = "TINYINT UNSIGNED")
    private Short careerYears;

    /** MySQL JSON 배열 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "service_categories", columnDefinition = "json")
    private List<String> serviceCategories;

    /** 활동 지역 배열(JSON) */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "regions", columnDefinition = "json")
    private List<String> regions;

    @Lob
    @Column(name = "intro")
    private String intro;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 편의 메서드 🔧
    public void linkUser(Users user) {
        this.user = user;
        //this.userId = (user != null ? user.getId() : null);
        if (user != null && user.getCaregiverProfile() != this) {
            user.attachCaregiverProfile(this);
        }
    }
}
