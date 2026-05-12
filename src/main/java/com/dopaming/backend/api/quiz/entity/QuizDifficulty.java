package com.dopaming.backend.api.quiz.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuizDifficulty {
    EASY("한 자리 수 덧셈/뺄셈"),
    MEDIUM("두 자리 수 사칙연산"),
    HARD("곱셈, 나눗셈, 괄호 포함 계산");

    private final String description;
}
