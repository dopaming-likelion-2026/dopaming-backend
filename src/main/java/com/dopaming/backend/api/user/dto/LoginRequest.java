package com.dopaming.backend.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @Schema(description = "로그인 아이디", example = "testUser123")
    private String loginId;

    @Schema(description = "비밀번호", example = "password123!")
    private String password;
}