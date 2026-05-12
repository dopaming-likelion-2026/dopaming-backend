package com.dopaming.backend.api.quiz.dto.request;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import lombok.Getter;

@Getter
public class QuizDifficultyUpdateRequest {
    private QuizDifficulty difficulty;
}
