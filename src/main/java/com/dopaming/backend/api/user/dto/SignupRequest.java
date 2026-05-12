package com.dopaming.backend.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Jackson(JSON 파서)이 객체를 생성할 때 필요
public class SignupRequest {

    @Schema(description = "로그인 아이디", example = "testUser123")
    private String loginId;

    @Schema(description = "비밀번호", example = "password123!")
    private String password;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "사용자 닉네임", example = "도파민제로")
    private String nickname;
}