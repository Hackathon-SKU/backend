package com.hackathon.backend.global.Config;

import com.hackathon.backend.global.Exception.UserAccessDeniedHandler;
import com.hackathon.backend.global.Exception.UserAuthenticationEntryPoint;
import com.hackathon.backend.global.security.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // Spring Security의 웹 보안 기능을 활성화하는 어노테이션

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// ㄴ> 메서드 수준에서의 보안 처리를 활성화 해주는 어노테이션
// ㄴ> @Secure, @PreAuthorize 어노테이션 사용 가능함
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfig corsConfig;

    private final UserAccessDeniedHandler accessDeniedHandler; // 인증은 됐지만 사용자의 권한(role)이 부족한 경우 사용할 핸들러
    private final UserAuthenticationEntryPoint authenticationEntryPoint; // 미인증된 사용자가 보호된 리소스에 접근 시 사용할 핸들러

    // 인증 없이 접근 가능한 화이트 리스트 URL 모음 String 배열 (로그인, 회원가입, 스웨거 등)
    private static final String[] AUTH_WHITELIST = {
            "/index.html",
            "/favicon.ico",
            "/assets/**", "/css/**", "/js/**", "/images/**",
            "/auth/login", "/auth/join", "/swagger-ui/**", "/api-docs", "swagger-ui-custom.html",
            "/ws/**"
    };

    // 각 Request마다 해당 filterChain에 등록된 필터들이 순서대로 실행된다.
    // HttpSecurity는 SecurityFilterChain을 조립하는 Builder(DSL)이다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 비활성화, CORS 기본값 적용
        http.csrf(AbstractHttpConfigurer::disable); // CSRF 방어 기능 비활성화
        // ㄴ> JWT 기반 Stateless API에서는 "폼 로그인-세션 기반" 인증이 아니므로 CSRF 방어 기능이 필요없다
        http.cors(Customizer.withDefaults()); // Spring MVC에서 등록한 CORS 기본 설정값을 적용
        // ㄴ> Spring Security의 CORS 설정을 활성화하되, 등록된 기본 CORS 설정(CorsConfigurationSource 빈)을 그대로 쓰겠다는 뜻
        // ㄴ> 교차 출처 리소스 공유(Cross-Origin Resource Sharing, CORS)는 브라우저가 자신의 출처가 아닌 다른 어떤
        // 출처(도메인, 스킴 혹은 포트)로부터 자원을 로딩하는 것을 허용하도록 서버가 허가해주는 HTTP 헤더 기반 메커니즘
        // ex) https://domain-a.com에서 제공되는 프론트엔트 JS 코드가 fetch()를 사용하여 https://domain-b.com/data.json에 요청하는 경우
        // 기존 웹 브라우저는 다른 도메인(origin)에 AJAX 요청을 보낼 때 보안 정책(Same-Origin Policy)때문에 차단한다
        // 이때, 서버가 "Access-Control-Allow-Origin", "Access-Control-Allow-Methods"같은 헤더를 내려주면, 브라우저가 요청을 허용한다.
        // ex) 프론트 : http://localhost:3000 & 백엔드 : http://localhost:8080
        // ㄴ> 서로 다른 origin이므로, CORS가 필요한 것이다.
        // 세밀한 설정을 위해서는 "CorsConfigurationSource" bean을 따로 등록해야한다


        // 2. 세션 관리 - 세션을 사용하지 않도록 설정한다. (JWT를 사용하기 위함임)
        http.sessionManagement(sessionManageMent -> sessionManageMent.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        ));

        // 3. 폼 로그인 & HTTP Basic 끄기
        // 폼 로그인(세션)과 Basic 인증을 꺼서 JWT만 사용하도록 만든다.
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 4. JWT 필터 등록
        // UsernamePasswordAuthenticationFilter 앞에 직접 만든 JwtAuthFilter를 넣는다
        // 해당 필터로 JWT를 통한 인증 처리를 수행하게 한다.
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // ㄴ> http.addFilterBefore("추가할 필터", 기존 필터)

        // 5. 예외 처리 핸들러 설정 - 인증 실패 및 접근 거부 예외를 처리하는 핸들러를 설정함
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPoint) // authenticationEntryPoint(401) : 인증 자체가 없는 상태에서 보호된 자원에 접근할 때 사용하는 핸들러
                // ㄴ> 내부적으로 commence 함수를 오버라이드 해놓았다.
                .accessDeniedHandler(accessDeniedHandler)); // accessDeniedHandler(403) : 인중은 됐지만 권한이 부족한 경우 사용되는 핸들러
        // ㄴ> 내부적으로 handler 함수 정의해놓음

        // 6. 권한 규칙 작성
        // 1) 화이트 리스트에 있는 경로는 누구나 접근할 수 있도록 허용함
        // 2) 나머지 모든 경로는 @PreAuthorize 등의 메서드 수준 보안을 사용하여 접근을 제어함
        http.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(AUTH_WHITELIST).permitAll()
                    .anyRequest().permitAll();
        });
        // authorizeHttpRequests() : Spring Security에서 URL 요청 별 인가(Authorization, 권한 부여)규칙을 설정하는 DSL 함수
        // 파라미터 : Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
        // ㄴ> 의미 : 인가 규칙 빌터(AuthorizationManagerRequestMatcherRegistry)를 커스터마이징하라
        // .requestMatchers(...) : URL 패턴, HttpMethod, RequestMatcher 등을 지정할 수 있음

        return http.build(); // build() 함수로 HttpSecurity 객체를 빌드하여 SecurityFilterChain 객체를 생성함
    }

    // AuthenticationManager를 Bean으로 등록해야 컨트롤러/서비스에서 주입이 가능함
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // BCryptPasswordEncoder : Spring Security 프레임워크에서 제공하는 비밀번호를 암호화하는 데 사용할 수 있는 메서드를 가진 클래스
    // BCrypt 해싱 함수를 사용해서 비밀번호를 인코딩할 수 있음
    // 패스워드 인코더도 Bean으로 등록하기 => Spring Security가 AuthenticationProvider -> UserDetailsService -> PasswordEncoder 과정을 처리해줄 때
    // 사용 할 수 있도록 하기 위함
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        CorsFilter corsFilter = new CorsFilter(corsConfigurationSource());
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(0); // ✅ 최우선 실행 (Security 필터보다 앞)
        return bean;
    }



    // 👇 여기서 CORS 허용 도메인/메서드/헤더를 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();

        // ⚠️ allowCredentials=true와 함께 * 는 불가 → "정확한 오리진" 또는 패턴 사용
        // trycloudflare 주소가 매번 바뀌면 '패턴'을 쓰세요.
        c.setAllowedOriginPatterns(List.of(
                "https://*.trycloudflare.com",
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://[::1]:5173",         // 일부 환경에서 로컬 IPv6
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://[::1]:3000",
                // (같은 Wi-Fi에서 휴대폰/다른PC로 접속한다면 아래처럼 사설IP도)
                "http://192.168.0.0:5173",
                "http://192.168.0.0:3000",
                // ↑ 예시는 placeholder, 실제 너의 내부 IP 대역/주소로 추가
                "https://*.ngrok.io",
                "https://*.ngrok-free.app"
        ));


        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));

        // 🍪 쿠키 주고받기 허용
        c.setAllowCredentials(true);

        // 프론트가 Authorization 헤더 쓰면 노출할 헤더 선언(선택)
        c.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s;
    }

    // (선택) 프록시/터널 뒤에서 https 스킴 감지 보정
    @Bean
    public org.springframework.web.filter.ForwardedHeaderFilter forwardedHeaderFilter() {
        return new org.springframework.web.filter.ForwardedHeaderFilter();
    }
}
