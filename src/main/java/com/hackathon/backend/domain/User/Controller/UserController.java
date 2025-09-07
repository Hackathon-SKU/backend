package com.hackathon.backend.domain.User.Controller;


import com.hackathon.backend.domain.User.Dto.CreateUserRequest;
import com.hackathon.backend.domain.User.Dto.DeleteByEmailRequest;
import com.hackathon.backend.domain.User.Entity.User;
import com.hackathon.backend.domain.User.Repository.UserRepository;
import com.hackathon.backend.domain.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "User Test", description = "Cloud SQL 연결 검증용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "DB 핑 테스트 (SELECT 1 아님, 단순 응답)", description = "애플리케이션 레벨 헬스 확인")
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok");
    }

    @Operation(summary = "유저 생성", description = "Cloud SQL(MySQL) 연동하여 INSERT 되는지 확인")
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody CreateUserRequest req) {
        return ResponseEntity.ok(userService.create(req));
    }

    @Operation(summary = "전체 조회", description = "JPA SELECT 테스트")
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "ID로 삭제", description = "JPA DELETE 테스트 - PathVariable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(Map.of("deletedId", id));
    }

    @Operation(summary = "이메일로 삭제", description = "JPA DELETE 테스트 - RequestBody(email)")
    @DeleteMapping("/by-email")
    public ResponseEntity<Map<String, Object>> deleteByEmail(@Valid @RequestBody DeleteByEmailRequest req) {
        userService.deleteByEmail(req.email());
        return ResponseEntity.ok(Map.of("deletedEmail", req.email()));
    }
}
