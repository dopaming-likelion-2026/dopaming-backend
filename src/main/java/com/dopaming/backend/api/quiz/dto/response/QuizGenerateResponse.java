package com.dopaming.backend.api.quiz.dto.response;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "생성된 퀴즈 정보 응답")
public class QuizGenerateResponse {

    @Schema(description = "퀴즈 고유 ID", example = "101")
    private Long quizId;

    @Schema(description = "출제된 퀴즈 문제 내용", example = "도파민 중독을 방지하기 위한 가장 좋은 습관은?")
    private String question;

    @Schema(description = "퀴즈 난이도", example = "NORMAL")
    private QuizDifficulty difficulty;
}