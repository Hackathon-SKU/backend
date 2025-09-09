package com.hackathon.backend.domain.User.Service;


import com.hackathon.backend.domain.User.Dto.Request.DeleteRequestDto;
import com.hackathon.backend.domain.User.Dto.Response.InfoResponseDto;
import com.hackathon.backend.domain.User.Repository.UserRepository;
import com.hackathon.backend.global.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    // 회원 탈퇴 함수
    @Transactional // 이 어노테이션을 붙여주지 않으면 오류 발생함
    public void delete(DeleteRequestDto deleteRequest) {
        // 전부 맞은 경우, 해당 사용자를 삭제한다.
        userRepository.deleteByEmail(deleteRequest.getEmail());
    }


    // 회원 정보 조회 함수
    @Transactional
    public InfoResponseDto info(CustomUserDetails userDetails) {
        return InfoResponseDto.builder()
                    .id(userDetails.getUser().getUserId())
                    .name(userDetails.getUser().getName())
                    .email(userDetails.getUser().getEmail())
                    .build();
    }
}
