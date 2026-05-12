package com.dopaming.backend.api.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.dopaming.backend.api.quiz.entity.QuizDifficulty;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id // Primary Key(PK) 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 알아서 1, 2, 3... 번호를 매기도록 설정
    private Long id;

    @Column(nullable = false, unique = true) // 필수 입력, 중복 불가
    private String loginId;

    @Column(nullable = false)
    private String password; // 향후 보안을 위해 반드시 암호화(BCrypt)된 해시값이 들어갈 자리

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    // 도파민 관리 기능을 위한 필드 (기본값 1800초 설정)
    @Column(nullable = false)
    private int dailyLimitSeconds = 1800; // =30분

    // 사용자의 현재 퀴즈 난이도 (기본값: EASY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizDifficulty quizDifficulty = QuizDifficulty.EASY;

    @Builder // 객체를 생성할 때 가독성 좋게 만들기 위한 롬복 어노테이션
    public User(String loginId, String password, String name, String nickname, int dailyLimitSeconds) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        // 값이 들어오면 그 값으로 세팅, 아니면 기본값 유지
        if (dailyLimitSeconds > 0) {
            this.dailyLimitSeconds = dailyLimitSeconds;
        }
    }

    // 하루 제한 시간 변경
    public void updateDailyLimit(int dailyLimitSeconds) {
        this.dailyLimitSeconds = dailyLimitSeconds;
    }

    // 퀴즈 난이도 변경
    public void updateQuizDifficulty(QuizDifficulty difficulty) {
        this.quizDifficulty = difficulty;}
}