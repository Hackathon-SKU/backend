package com.hackathon.backend.domain.User.Repository;

import com.hackathon.backend.domain.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 만약 필요하다면 추가 쿼리 메소드 정의하기

    User findByEmail(String email);
    boolean existsByEmail(String email); // 이메일로 중복 확인하는 함수
    void deleteByEmail(String email);
}
