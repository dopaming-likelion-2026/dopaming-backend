package com.dopaming.backend.api.user.controller;

import com.dopaming.backend.api.user.dto.LoginRequest;
import com.dopaming.backend.api.user.dto.SignupRequest;
import com.dopaming.backend.api.user.dto.TokenRequest;
import com.dopaming.backend.api.user.service.AuthService;
// Swagger 어노테이션 추가
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "인증(로그인, 회원가입 등) 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "아이디 중복 확인", description = "입력한 로그인 아이디가 이미 사용 중인지 확인합니다.")
    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam("loginId") String loginId) {
        boolean isAvailable = authService.checkIdAvailable(loginId);

        if (isAvailable) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "사용 가능한 아이디입니다.",
                    "data", Map.of("isAvailable", true)
            ));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "error",
                    "message", "이미 사용 중인 아이디입니다.",
                    "code", "DUPLICATE_ID",
                    "data", Map.of("isAvailable", false)
            ));
        }
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        var responseData = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status", "success",
                "message", "회원가입이 성공적으로 완료되었습니다.",
                "data", responseData
        ));
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하여 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var tokenData = authService.login(request);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "로그인에 성공하였습니다.",
                "data", tokenData
        ));
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequest request) {
        var newTokenData = authService.reissueToken(request.getRefreshToken());
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "토큰이 성공적으로 재발급되었습니다.",
                "data", newTokenData
        ));
    }

    @Operation(summary = "로그아웃", description = "현재 사용자의 Access Token을 무효화하여 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "성공적으로 로그아웃되었습니다."
        ));
    }
}