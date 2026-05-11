package com.dopaming.backend.api.shortform.dto.response;

import com.dopaming.backend.api.shortform.entity.ShortformUsageStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortformTodayResponse {

    @Schema(description = "오늘 누적 숏폼 사용 시간, 초 단위", example = "1200")
    private int todayUsageSeconds;

    @Schema(description = "하루 숏폼 제한 시간, 초 단위", example = "1800")
    private int dailyLimitSeconds;

    @Schema(description = "퀴즈 정답으로 추가 허용된 시간, 초 단위", example = "300")
    private int extraTimeSeconds;

    @Schema(description = "오늘 총 숏폼 허용 시간, 초 단위", example = "2100")
    private int totalAllowedSeconds;

    @Schema(description = "오늘 남은 숏폼 사용 가능 시간, 초 단위", example = "600")
    private int remainingSeconds;

    @Schema(description = "사용 가능 상태", example = "AVAILABLE")
    private ShortformUsageStatus status; // AVAILABLE, QUIZ_REQUIRED
}