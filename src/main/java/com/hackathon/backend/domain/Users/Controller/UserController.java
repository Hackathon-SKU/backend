package com.hackathon.backend.domain.Users.Controller;
import com.hackathon.backend.domain.Users.Dto.Request.DeleteRequestDto;
import com.hackathon.backend.domain.Users.Dto.Response.InfoResponseDto;
import com.hackathon.backend.domain.Users.Service.UserService;
import com.hackathon.backend.global.Response.GlobalWebResponse;
import com.hackathon.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.global.Response.GlobalWebResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="User", description = "인증된 사용자 관련 기능을 수행하는 API")
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

    @Operation(
            summary = "회원 탈퇴",
            description =
                    """
                    **로그인된 상태**에서 사용자 이름, 이메일, 비밀번호를 입력받아서 해당 사용자의 탈퇴를 진행합니다.
                    
                    - 성공 시:
                        - "회원 탈퇴 성공" 메시지가 담긴 body return 됩니다. (중요 데이터는 return 되지 않음)
                    """
    )
    @DeleteMapping("/delete")
    public ResponseEntity<GlobalWebResponse<String>> delete(@RequestBody @Valid DeleteRequestDto deleteRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.delete(deleteRequest);
        GlobalWebResponse<String> response = success("200", "회원 탈퇴 성공", null);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "사용자 정보 반환",
            description =
                    """
                    **로그인된 상태**에서 해당 사용자의 정보를 반환해줍니다.
                    
                    - 성공 시 :
                        - 사용자 id(DB PK)
                        - 사용자 이름
                        - 사용자 이메일
                        - 사용자 포인트
                        - 사용자 프로필 이미지 url
                        을 반환해줍니다.
                    """
    )
    @GetMapping("/info")
    public ResponseEntity<GlobalWebResponse<InfoResponseDto>> info(@AuthenticationPrincipal CustomUserDetails userDetails){
        // (Authentication authentication으로 매핑 시)
            // ㄴ> Spring Security 필터를 거칠 때, 클라이언트의 Authentication 헤더에 있는 토큰 값으로 Authenticaion 객체가 만들어져 해당 함수의 인자에 매핑된다.
            // 이렇게 데이터를 꺼내올 때, 서버에서 계속 해당 authentication 객체를 가지고 있는게 아니라, 1번의 요청에 매번 authentication 객체를 생성하고, 해당 요청 로직 중에
            // 처음 만들었던 authentication 객체에서 데이터를 얻어서 사용하는 것이다. 이후, 해당 요청이 끝나면 해당 authentication 객체를 SecurityContext에서 삭제하는 것이다!!


        InfoResponseDto infoResponseDto = userService.info(userDetails);
        return ResponseEntity.ok(GlobalWebResponse.success("사용자 정보 조회 성공", infoResponseDto));
    }

}
