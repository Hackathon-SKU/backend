package com.hackathon.backend.domain.User.Controller;
import com.hackathon.backend.domain.User.Dto.Request.DeleteRequestDto;
import com.hackathon.backend.domain.User.Dto.Response.InfoResponseDto;
import com.hackathon.backend.domain.User.Service.UserService;
import com.hackathon.backend.global.Response.GlobalWebResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.global.Response.GlobalWebResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
// UserController는 사용자 리소스 관련 작업만 수행한다
// 즉, 이미 인증된 사용자만 가능한 작업을 정의하는 것이다. => SecurityContextHolder에서 인증된 사용자 정보를 꺼내쓴다
public class UserController {
//    // JWT 정상적으로 .env 설정 되었는지 디버깅용 코드
//    @Value("${jwt.secret:__missing__}")
//    private String secret;
//
//    @PostConstruct
//    void check() {
//        System.out.println("JWT secret = " + secret);
//    }

    private final UserService userService;



    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<GlobalWebResponse<String>> delete(@RequestBody @Valid DeleteRequestDto deleteRequest) {
        userService.delete(deleteRequest);
        GlobalWebResponse<String> response = success("200", "회원 탈퇴 성공", null);
        return ResponseEntity.ok(response);
    }


    // 회원 정보 get
    @GetMapping("/info")
    public ResponseEntity<GlobalWebResponse<InfoResponseDto>> info(Authentication authentication){
        // ㄴ> Spring Security 필터를 거칠 때, 클라이언트의 Authentication 헤더에 있는 토큰 값으로 Authenticaion 객체가
        // 만들어져 해당 함수의 인자에 매핑된다.
        InfoResponseDto infoResponseDto = userService.info(authentication);
        return ResponseEntity.ok(GlobalWebResponse.success("사용자 정보 조회 성공", infoResponseDto));
    }

}
