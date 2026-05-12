package com.dopaming.backend.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 DTO (현재는 TokenResponse를 주력으로 사용 중)")
public class LoginResponse {

    // TODO: 만약 TokenResponse 대신 LoginResponse에 고유한 응답 필드를 추가해야 한다면 아래 형식을 참고하세요.
    // @Schema(description = "사용자 ID", example = "1")
    // private Long userId;
}