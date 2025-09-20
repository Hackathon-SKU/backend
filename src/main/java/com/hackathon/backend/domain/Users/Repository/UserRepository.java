package com.hackathon.backend.domain.Users.Repository;

import com.hackathon.backend.domain.Users.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // 만약 필요하다면 추가 쿼리 메소드 정의하기

    Users findByEmail(String email);
    boolean existsByEmail(String email); // 이메일로 중복 확인하는 함수
    void deleteByEmail(String email);
}
