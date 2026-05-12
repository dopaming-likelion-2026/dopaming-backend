package com.dopaming.backend.api.quiz.dto.request;

import lombok.Getter;

@Getter
public class QuizSubmitRequest {
    private Long quizId;
    private int answer;
}