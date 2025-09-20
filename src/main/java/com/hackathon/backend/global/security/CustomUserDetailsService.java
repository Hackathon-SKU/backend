package com.hackathon.backend.global.security;

import com.hackathon.backend.domain.Users.Entity.Users;
import com.hackathon.backend.domain.Users.Repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
@Builder
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    // username 파라미터를 "email"로 사용한다 (로그인할 때는 이메일로 로그인한다.)
    // AuthenticationProvider가 해당 메서드를 호출해 DB 사용자를 로딩함 => UserDetails 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 현재 로직에서는 username을 email로 가정
        String email = username;

        // DB에서 email로 사용자 정보 가져옴
        Users users = userRepository.findByEmail(email);

        // 이제 해당 사용자 정보를 CustomUserDetails 객체로 return해줄 것이다.
        // 이때, CustomUserDetails를 생성할 때 사용되는 DTO인 JwtUserInfoDto를 생성한다.
        JwtUserInfoDto dto = JwtUserInfoDto.builder()
                .userId(users.getId())
                .email(users.getEmail())
                .password(users.getPasswordHash())
                .name(users.getName())
                .role(users.getRole())
                .build();

        // JWTUserInfoDto를 사용해서 Security가 이해할 수 있는 형태인 UserDetails로 래핑한다.
        return CustomUserDetails.builder()
                .user(dto)
                .build();
    }

    // username 파라미터를 "id"로 사용한다
    // AuthenticationProvider가 해당 메서드를 호출해 DB 사용자를 로딩함 => UserDetails 반환
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {

        // DB에서 email로 사용자 정보 가져옴
        Optional<Users> user = userRepository.findById(userId);

        // 이제 해당 사용자 정보를 CustomUserDetails 객체로 return해줄 것이다.
        // 이때, CustomUserDetails를 생성할 때 사용되는 DTO인 JwtUserInfoDto를 생성한다.
        JwtUserInfoDto dto = JwtUserInfoDto.builder()
                .userId(user.get().getId())
                .email(user.get().getEmail())
                .password(user.get().getPasswordHash())
                .name(user.get().getName())
                .role(user.get().getRole())
                .build();

        // JWTUserInfoDto를 사용해서 Security가 이해할 수 있는 형태인 UserDetails로 래핑한다.
        return CustomUserDetails.builder()
                .user(dto)
                .build();
    }


}
