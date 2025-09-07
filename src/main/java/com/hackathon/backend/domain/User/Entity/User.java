package com.hackathon.backend.domain.User.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users") // 이미 user 테이블이 있다면 "user" 로 변경
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String role; // USER / ADMIN 등

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
