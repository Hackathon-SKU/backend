package com.hackathon.backend.domain.Auth.Controller;

import com.hackathon.backend.domain.Auth.Dto.Request.LoginRequestDto;
import com.hackathon.backend.domain.Auth.Dto.Response.LoginResponseDto;
import com.hackathon.backend.domain.Auth.Service.AuthService;
import com.hackathon.backend.domain.Auth.Dto.Request.JoinRequestDto;
import com.hackathon.backend.domain.Auth.Dto.Response.JoinResponseDto;
import com.hackathon.backend.global.Response.GlobalWebResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Auth", description = "사용자 인증 관련 API") // 클래스(Controller) 그룹핑할 때 사용

// AuthController는 인증 관련 작업만 수행한다
// 토큰 발급/인증 흐름 관리에만 집중함 - 로그인, 회원가입, 토큰 재발급, 로그아웃
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "회원가입", // Swagger UI에 제목처럼 보임
            description =
                    """
                    사용자 이름, 이메일, 비밀번호, 프로필 이미지 url 정보를 입력받아 회원가입을 수행합니다.
                    회원가입이 되었다면, 자동 로그인이 수행됩니다.
                    - 성공 시 : 
                        - "Authorization" 헤더에 엑세스 토큰이 포함됨
                        - Set-Cookie로 클라이언트의 쿠키에 refresh 토큰이 저장됨  
                    """
    )
    @PostMapping("/join")
    public ResponseEntity<GlobalWebResponse<JoinResponseDto>> join(@RequestBody @Valid JoinRequestDto joinRequestDto, HttpServletResponse response){
        JoinResponseDto joinResponseDto = authService.join(joinRequestDto, response);



        // 만약 RESTful API 규약을 엄격하게 지킨다면, Location 헤더를 사용해서 HTTP 표준인 "201 Created" 응답의 관례를 지키면 된다.
        GlobalWebResponse<JoinResponseDto> dto = GlobalWebResponse.success("회원가입 성공", joinResponseDto);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "로그인",
            description =
                    """
                    사용자 이메일, 비밀번호로 로그인을 수행합니다.
                    - 성공 시 : 
                        - "Authorization" 헤더에 엑세스 토큰이 포함됨
                        - Set-Cookie로 클라이언트의 쿠키에 refresh 토큰이 저장됨 
                    """
    )
    @PostMapping("/login")
    // 사용자 정보를 바탕으로 로그인 및 jwt 발급 수행
    public ResponseEntity<GlobalWebResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto dto = authService.login(loginRequestDto, response);
        return ResponseEntity.ok(GlobalWebResponse.success("로그인 성공", dto));
    }

    @Operation(
            summary = "로그아웃",
            description =
                    """
                    현재 로그인된 사용자를 로그아웃 합니다.
                    - 요청 Headers의 "Authorization" 헤더에서 access 토큰을 추출하여 로그아웃 처리합니다.
                    - access 토큰은 Redis 블랙리스트에 등록되어 해당 토큰의 만료시간 전까지 재사용이 차단됩니다.
                    - refresh 토큰은 Redis에서 삭제되며, **브라우저 쿠키에서도 삭제됩니다.**
                    - 응답 body에는 별도의 데이터가 없으며, 로그아웃 성공 메시지만 반환됩니다. 
                    """
    )
    @PostMapping("/logout")
    public ResponseEntity<GlobalWebResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response){
        authService.logout(request, response);
        return ResponseEntity.ok(GlobalWebResponse.success("로그아웃 성공"));
    }


    @Operation(
            summary = "Access Token 재발급",
            description =
                    """
                    사용자가 갖고 있는 Access Token이 만료되었을 때(JWT_401_004 코드), 클라이언트 쿠키에 저장되어있는 Refresh Token을 이용하여
                    Access Token 재발급 요청을 합니다.
                    - request body는 필요 없습니다.
                    - 성공 시:
                        - "Access Token 재발급 성공" 메시지만 response에 담깁니다.
                    """
    )
    @PostMapping("/refresh")
    public ResponseEntity<GlobalWebResponse<Void>> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        authService.reissueAccessToken(request, response);
        return ResponseEntity.ok(GlobalWebResponse.success("Access Token 재발급 성공"));
    }

}
