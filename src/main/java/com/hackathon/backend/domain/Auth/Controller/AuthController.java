package com.hackathon.backend.domain.Auth.Controller;

import com.hackathon.backend.domain.Auth.Dto.Request.LoginRequestDto;
import com.hackathon.backend.domain.Auth.Dto.Response.LoginResponseDto;
import com.hackathon.backend.domain.Auth.Service.AuthService;
import com.hackathon.backend.domain.Auth.Dto.Request.JoinRequestDto;
import com.hackathon.backend.domain.Auth.Dto.Response.JoinResponseDto;
import com.hackathon.backend.global.Response.GlobalWebResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hackathon.backend.global.Response.GlobalWebResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

// AuthController는 인증 관련 작업만 수행한다
// 토큰 발급/인증 흐름 관리에만 집중함 - 로그인, 회원가입, 토큰 재발급, 로그아웃
public class AuthController {
    private final AuthService authService;

    // UserService의 join() 함수를 이용하여 회원가입 진행
    @PostMapping("/join")
    public ResponseEntity<GlobalWebResponse<JoinResponseDto>> join(@RequestBody @Valid JoinRequestDto joinRequestDto, HttpServletResponse response){
        JoinResponseDto joinResponseDto = authService.join(joinRequestDto, response);

        // 만약 RESTful API 규약을 엄격하게 지킨다면, Location 헤더를 사용해서 HTTP 표준인 "201 Created" 응답의 관례를 지키면 된다.
        GlobalWebResponse<JoinResponseDto> dto = success("회원가입 성공", joinResponseDto);
        return ResponseEntity.ok(dto);
    }

    // 사용자 정보를 바탕으로 로그인 및 jwt 발급 수행
    @PostMapping("/login")
    public ResponseEntity<GlobalWebResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto dto = authService.login(loginRequestDto, response);
        return ResponseEntity.ok(GlobalWebResponse.success("로그인 성공", dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<GlobalWebResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response){
        authService.logout(request, response);
        return ResponseEntity.ok(GlobalWebResponse.success("로그아웃 성공"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<GlobalWebResponse<Void>> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        authService.reissueAccessToken(request, response);
        return ResponseEntity.ok(GlobalWebResponse.success("Access Token 재발급 성공"));
    }



}
