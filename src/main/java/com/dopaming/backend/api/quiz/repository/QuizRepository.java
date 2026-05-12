package com.dopaming.backend.api.quiz.repository;

import com.dopaming.backend.api.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}