package com.dopaming.backend.api.quiz.dto.response;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "난이도 변경 결과 응답")
public class QuizDifficultyUpdateResponse {

    @Schema(description = "변경된 난이도", example = "HARD")
    private QuizDifficulty changedDifficulty;

    @Schema(description = "변경 결과 메시지", example = "퀴즈 난이도가 성공적으로 변경되었습니다.")
    private String message;
}