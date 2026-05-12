package com.dopaming.backend.api.quiz.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "퀴즈 정답 제출 결과 응답")
public class QuizSubmitResponse {

    @Schema(description = "정답 여부", example = "true")
    private boolean isCorrect;

    @Schema(description = "정답 시 연장된 시간(초)", example = "300")
    private int extendedSeconds;

    @Schema(description = "결과 메시지", example = "정답입니다! 숏폼 시청 시간이 300초 연장되었습니다.")
    private String message;
}