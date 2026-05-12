package com.dopaming.backend.api.quiz.service;

import com.dopaming.backend.api.quiz.dto.request.QuizDifficultyUpdateRequest;
import com.dopaming.backend.api.quiz.dto.request.QuizSubmitRequest;
import com.dopaming.backend.api.quiz.dto.response.*;
import com.dopaming.backend.api.quiz.entity.Quiz;
import com.dopaming.backend.api.quiz.entity.QuizDifficulty;
import com.dopaming.backend.api.quiz.repository.QuizRepository;
import com.dopaming.backend.api.shortform.service.ShortformService;
import com.dopaming.backend.api.user.entity.User;
import com.dopaming.backend.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final ShortformService shortformService;

    @Transactional
    public QuizGenerateResponse generateQuiz(String loginId) {
        User user = getUser(loginId);
        QuizDifficulty difficulty = user.getQuizDifficulty();

        Random random = new Random();
        int a, b, answer;
        String question;

        switch (difficulty) {
            case EASY:
                a = random.nextInt(9) + 1;
                b = random.nextInt(9) + 1;
                answer = a + b;
                question = String.format("%d + %d = ?", a, b);
                break;
            case MEDIUM:
                a = random.nextInt(90) + 10;
                b = random.nextInt(90) + 10;
                answer = a + b;
                question = String.format("%d + %d = ?", a, b);
                break;
            case HARD:
                a = random.nextInt(9) + 1;
                b = random.nextInt(9) + 1;
                int c = random.nextInt(5) + 2;
                answer = (a + b) * c;
                question = String.format("( %d + %d ) * %d = ?", a, b, c);
                break;
            default:
                throw new IllegalStateException("알 수 없는 난이도입니다.");
        }

        Quiz quiz = Quiz.builder()
                .user(user)
                .question(question)
                .answer(answer)
                .difficulty(difficulty)
                .build();
        quizRepository.save(quiz);

        return QuizGenerateResponse.builder()
                .quizId(quiz.getId())
                .question(quiz.getQuestion())
                .difficulty(quiz.getDifficulty())
                .build();
    }

    @Transactional
    public QuizSubmitResponse submitQuiz(String loginId, QuizSubmitRequest request) {
        User user = getUser(loginId); // loginId로 유저 찾기

        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

        // 유저 ID 끼리 비교 (보안 검증)
        if (!quiz.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 퀴즈만 풀 수 있습니다.");
        }

        if (quiz.isSolved()) {
            throw new IllegalArgumentException("이미 정답을 맞춘 퀴즈입니다.");
        }

        boolean isCorrect = (quiz.getAnswer() == request.getAnswer());

        if (isCorrect) {
            quiz.markAsSolved();
            // User 엔티티에서 꺼낸 숫자 ID(Long)를 ShortformService로 전달
            shortformService.addExtraTime(user.getId());

            return QuizSubmitResponse.builder()
                    .isCorrect(true)
                    .extendedSeconds(300)
                    .message("정답입니다! 이용 시간이 5분 연장되었습니다.")
                    .build();
        }

        return QuizSubmitResponse.builder()
                .isCorrect(false)
                .extendedSeconds(0)
                .message("오답입니다. 다시 도전해보세요!")
                .build();
    }

    public QuizDifficultyReadResponse getDifficulty(String loginId) {
        User user = getUser(loginId);
        QuizDifficulty currentDifficulty = user.getQuizDifficulty();

        List<QuizDifficultyReadResponse.DifficultyLevelDto> levels = Arrays.stream(QuizDifficulty.values())
                .map(diff -> QuizDifficultyReadResponse.DifficultyLevelDto.builder()
                        .level(diff)
                        .detail(diff.getDescription())
                        .build())
                .collect(Collectors.toList());

        return QuizDifficultyReadResponse.builder()
                .currentDifficulty(currentDifficulty)
                .description(currentDifficulty.getDescription())
                .difficultyLevels(levels)
                .build();
    }

    @Transactional
    public QuizDifficultyUpdateResponse updateDifficulty(String loginId, QuizDifficultyUpdateRequest request) {
        User user = getUser(loginId);
        QuizDifficulty newDifficulty = request.getDifficulty();
        user.updateQuizDifficulty(newDifficulty);

        return QuizDifficultyUpdateResponse.builder()
                .changedDifficulty(newDifficulty)
                .message(String.format("난이도가 '%s'로 변경되었습니다.", newDifficulty.name()))
                .build();
    }

    // ⭐ 유저 조회 공통 메서드 (findById -> findByLoginId 로 변경됨)
    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}