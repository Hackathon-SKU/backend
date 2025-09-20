package com.hackathon.backend.domain.Profiles.Entity;


import com.hackathon.backend.domain.Users.Entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "disabled_profiles",
        indexes = {
                @Index(name = "idx_disabled_profiles_region", columnList = "region")
        }
)
public class DisabledProfile {

    /** 공유 PK = FK */
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** Users <-> DisabledProfile : 1:1, Shared PK */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "registration_number", length = 30)
    private String registrationNumber;

    /** MySQL JSON */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "classification", columnDefinition = "json")
    private Map<String, Object> classification;

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
        if (user != null && user.getDisabledProfile() != this) {
            user.attachServiceUserProfile(this);
        }
    }
}
