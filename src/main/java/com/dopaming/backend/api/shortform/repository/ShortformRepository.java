package com.dopaming.backend.api.shortform.repository;

import com.dopaming.backend.api.shortform.entity.Shortform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortformRepository extends JpaRepository<Shortform, Long> {
}