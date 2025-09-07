package com.hackathon.backend.domain.User.Service;


import com.hackathon.backend.domain.User.Dto.CreateUserRequest;
import com.hackathon.backend.domain.User.Entity.User;
import com.hackathon.backend.domain.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(CreateUserRequest req) {
        // 실제 서비스에서는 비밀번호 반드시 BCrypt 등으로 암호화!
        var entity = User.builder()
                .email(req.email())
                .name(req.name())
                .password(req.password())
                .role(req.role())
                .build();
        return userRepository.save(entity);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }
}
