package com.dopaming.backend.api.quiz.entity;

import com.dopaming.backend.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String question; // 예: "12 + 45"

    @Column(nullable = false)
    private int answer; // 예: 57

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizDifficulty difficulty;

    private boolean isSolved = false; // 정답을 맞췄는지 여부

    @Builder
    public Quiz(User user, String question, int answer, QuizDifficulty difficulty) {
        this.user = user;
        this.question = question;
        this.answer = answer;
        this.difficulty = difficulty;
    }

    public void markAsSolved() {
        this.isSolved = true;
    }
}