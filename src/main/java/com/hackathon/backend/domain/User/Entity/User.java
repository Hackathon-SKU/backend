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
    @Column(name="id", nullable=false)
    private Long id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="email", length = 50)
    private String email;

    @Column(name = "point")
    @Builder.Default // Builder에서 초기값을 반영하려면, 해당 어노테이션을 적용해야한다.
    private Integer point=0;

    @Enumerated(EnumType.STRING) // Enum 이름 그대로가 DB에 저장되므로, RoleType에서 굳이 따로 변수를 지정해주지 않아도 된다.
    @Column(name = "role", nullable = false)
    private RoleType role;

    @Column(name = "profile_image", nullable = true)
    private String profileImageUrl;

    // Dto -> user
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
