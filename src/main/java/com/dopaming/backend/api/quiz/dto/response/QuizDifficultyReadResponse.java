package com.dopaming.backend.api.quiz.dto.response;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@Schema(description = "난이도 설정 정보 조회 응답")
public class QuizDifficultyReadResponse {

    @Schema(description = "현재 설정된 난이도", example = "NORMAL")
    private QuizDifficulty currentDifficulty;

    @Schema(description = "난이도 관련 안내 문구", example = "현재 난이도에 따라 퀴즈가 출제됩니다.")
    private String description;

    @Schema(description = "사용 가능한 전체 난이도 리스트")
    private List<DifficultyLevelDto> difficultyLevels;

    @Getter
    @Builder
    @Schema(description = "난이도 레벨 상세 정보")
    public static class DifficultyLevelDto {
        @Schema(description = "난이도 레벨", example = "EASY")
        private QuizDifficulty level;

        @Schema(description = "난이도에 대한 상세 설명", example = "기본적인 상식 수준의 퀴즈가 출제됩니다.")
        private String detail;
    }
}