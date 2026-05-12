package com.dopaming.backend.api.quiz.dto.response;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class QuizDifficultyReadResponse {
    private QuizDifficulty currentDifficulty;
    private String description;
    private List<DifficultyLevelDto> difficultyLevels;

    @Getter
    @Builder
    public static class DifficultyLevelDto {
        private QuizDifficulty level;
        private String detail;
    }
}