package com.dopaming.backend.api.quiz.dto.request;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "퀴즈 난이도 수정 요청")
public class QuizDifficultyUpdateRequest {

    @Schema(description = "변경할 퀴즈 난이도 (예: EASY, NORMAL, HARD)", example = "NORMAL")
    private QuizDifficulty difficulty;
}