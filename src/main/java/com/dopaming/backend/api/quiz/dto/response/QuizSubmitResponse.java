package com.dopaming.backend.api.quiz.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizSubmitResponse {
    private boolean isCorrect;
    private int extendedSeconds;
    private String message;
}