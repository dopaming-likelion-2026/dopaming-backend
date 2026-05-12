package com.dopaming.backend.api.quiz.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "퀴즈 정답 제출 요청")
public class QuizSubmitRequest {

    @Schema(description = "제출할 퀴즈의 고유 ID", example = "10")
    private Long quizId;

    @Schema(description = "사용자가 선택한 정답 번호 (보통 1~4번)", example = "2")
    private int answer;
}