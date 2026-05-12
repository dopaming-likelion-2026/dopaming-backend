package com.dopaming.backend.api.quiz.controller;

import com.dopaming.backend.api.quiz.dto.request.QuizDifficultyUpdateRequest;
import com.dopaming.backend.api.quiz.dto.request.QuizSubmitRequest;
import com.dopaming.backend.api.quiz.dto.response.*;
import com.dopaming.backend.api.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Quiz", description = "퀴즈(퀴즈 생성, 제출 및 난이도 조절) 관련 API")
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 생성", description = "사용자의 학습 상태나 도파민 관리 현황에 맞는 맞춤형 퀴즈를 생성합니다.")
    @PostMapping("/generate")
    public ResponseEntity<?> generateQuiz(@AuthenticationPrincipal UserDetails userDetails) {
        QuizGenerateResponse response = quizService.generateQuiz(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }

    @Operation(summary = "퀴즈 정답 제출", description = "사용자가 푼 퀴즈의 정답을 제출하고, 정답 여부에 따라 보상(시간 연장 등)을 처리합니다.")
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody QuizSubmitRequest request) {
        QuizSubmitResponse response = quizService.submitQuiz(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }

    @Operation(summary = "퀴즈 난이도 조회", description = "현재 사용자가 설정하여 풀고 있는 퀴즈의 난이도를 조회합니다.")
    @GetMapping("/difficulty")
    public ResponseEntity<?> getDifficulty(@AuthenticationPrincipal UserDetails userDetails) {
        QuizDifficultyReadResponse response = quizService.getDifficulty(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }

    @Operation(summary = "퀴즈 난이도 변경", description = "사용자의 수준에 맞춰 퀴즈 난이도를 업데이트합니다.")
    @PutMapping("/difficulty")
    public ResponseEntity<?> updateDifficulty(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody QuizDifficultyUpdateRequest request) {
        QuizDifficultyUpdateResponse response = quizService.updateDifficulty(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }
}