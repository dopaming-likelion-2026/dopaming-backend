package com.dopaming.backend.api.quiz.controller;

import com.dopaming.backend.api.quiz.dto.request.QuizDifficultyUpdateRequest;
import com.dopaming.backend.api.quiz.dto.request.QuizSubmitRequest;
import com.dopaming.backend.api.quiz.dto.response.*;
import com.dopaming.backend.api.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails; // 추가됨!
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateQuiz(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails.getUsername()은 나경 님의 설정상 loginId(이메일 등)를 가져옵니다!
        QuizGenerateResponse response = quizService.generateQuiz(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody QuizSubmitRequest request) {
        QuizSubmitResponse response = quizService.submitQuiz(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }

    @GetMapping("/difficulty")
    public ResponseEntity<?> getDifficulty(@AuthenticationPrincipal UserDetails userDetails) {
        QuizDifficultyReadResponse response = quizService.getDifficulty(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }

    @PutMapping("/difficulty")
    public ResponseEntity<?> updateDifficulty(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody QuizDifficultyUpdateRequest request) {
        QuizDifficultyUpdateResponse response = quizService.updateDifficulty(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("status", "success", "data", response));
    }
}