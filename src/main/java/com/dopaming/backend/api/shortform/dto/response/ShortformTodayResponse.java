package com.dopaming.backend.api.shortform.dto.response;

import com.dopaming.backend.api.shortform.entity.ShortformUsageStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortformTodayResponse {

    private int todayUsageSeconds;

    private int todayUsageMinutes;

    private int dailyLimit;

    private int dailyLimitMinutes;

    private int remainingSeconds;

    private int remainingMinutes;

    private ShortformUsageStatus status; // AVAILABLE, QUIZ_REQUIRED
}