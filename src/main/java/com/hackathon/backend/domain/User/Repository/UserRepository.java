package com.hackathon.backend.domain.User.Repository;

import com.hackathon.backend.domain.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
}
