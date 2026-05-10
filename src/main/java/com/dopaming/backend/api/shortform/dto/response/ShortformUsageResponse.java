package com.dopaming.backend.api.shortform.dto.response;

import com.dopaming.backend.api.shortform.entity.ShortformUsageStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortformUsageResponse {

    private int todayUsageSeconds;

    private int dailyLimit;

    private int remainingSeconds;

    private ShortformUsageStatus status; // AVAILABLE, QUIZ_REQUIRED
}