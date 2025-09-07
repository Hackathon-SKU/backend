package com.hackathon.backend.domain.User.Entity;

import com.hackathon.backend.domain.Auth.Dto.Request.JoinRequestDto;
import com.hackathon.backend.global.Jwt.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user") // 이미 user 테이블이 있다면 "user" 로 변경
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB의 AUTO_INCREMENT와 같은 방식
    @Column(name="user_id", nullable=false)
    private Long id;

    @Column(name="user_name", nullable=false)
    private String name;

    @Column(name="user_password", nullable=false)
    private String password;

    @Column(name="user_email", length = 50, updatable = false)
    private String email;

    @Enumerated(EnumType.STRING) // Enum 이름 그대로가 DB에 저장되므로, RoleType에서 굳이 따로 변수를 지정해주지 않아도 된다.
    @Column(name = "user_role", nullable = false)
    private RoleType role;

    @Column(name = "user_profile_image", nullable = true)
    private String profileImageUrl;

    public static User from(JoinRequestDto dto){
        return User.builder()
                .name(dto.getName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .role(RoleType.USER) // 무조건 사용자로 권한 설정
                .profileImageUrl(dto.getProfileImageUrl())
                .build();
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateName(String name){
        this.name = name;
    }

}
