package com.dopaming.backend.api.shortform.repository;

import com.dopaming.backend.api.shortform.entity.DailyShortform;
import com.dopaming.backend.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyShortformRepository extends JpaRepository<DailyShortform, Long> {

    Optional<DailyShortform> findByUserAndUsageDate(User user, LocalDate usageDate);
}