package com.dopaming.backend.api.quiz.dto.response;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizDifficultyUpdateResponse {
    private QuizDifficulty changedDifficulty;
    private String message;
}