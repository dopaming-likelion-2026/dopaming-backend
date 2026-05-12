package com.dopaming.backend.api.quiz.dto.response;

import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizGenerateResponse {
    private Long quizId;
    private String question;
    private QuizDifficulty difficulty;
}