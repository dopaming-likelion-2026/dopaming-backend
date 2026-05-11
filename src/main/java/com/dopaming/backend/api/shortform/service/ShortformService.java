package com.dopaming.backend.api.shortform.service;

import com.dopaming.backend.api.shortform.dto.request.ShortformLimitUpdateRequest;
import com.dopaming.backend.api.shortform.dto.request.ShortformUsageRequest;
import com.dopaming.backend.api.shortform.dto.response.ShortformLimitResponse;
import com.dopaming.backend.api.shortform.dto.response.ShortformTodayResponse;
import com.dopaming.backend.api.shortform.dto.response.ShortformUsageResponse;
import com.dopaming.backend.api.shortform.entity.DailyShortform;
import com.dopaming.backend.api.shortform.entity.Shortform;
import com.dopaming.backend.api.shortform.entity.ShortformUsageStatus;
import com.dopaming.backend.api.shortform.repository.DailyShortformRepository;
import com.dopaming.backend.api.shortform.repository.ShortformRepository;
import com.dopaming.backend.api.user.entity.User;
import com.dopaming.backend.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortformService {

    // 한국 시간대
    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    private final ShortformRepository shortformRepository;
    private final DailyShortformRepository dailyShortformRepository;
    private final UserRepository userRepository;

    // 숏폼 사용 시간을 저장하고, 오늘 누적 사용 시간과 제한 시간 초과 여부를 반환한다.
    @Transactional
    public ShortformUsageResponse recordUsage(Long userId, ShortformUsageRequest request) {
        User user = getUser(userId);

        validateUsageRequest(request);

        LocalDate today = LocalDate.now(KOREA_ZONE);

        Shortform shortform = Shortform.builder()
                .user(user)
                .platform(request.getPlatform())
                .usageDate(today)
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .durationSeconds(request.getDurationSeconds())
                .build();

        shortformRepository.save(shortform);

        DailyShortform dailyShortform = getOrCreateDailyShortform(user, today);
        dailyShortform.addUsageSeconds(request.getDurationSeconds());

        int todayUsageSeconds = dailyShortform.getTotalUsageSeconds();
        int dailyLimitSeconds = user.getDailyLimitSeconds();
        int extraTimeSeconds = dailyShortform.getExtraTimeSeconds();
        int totalAllowedSeconds = dailyLimitSeconds + extraTimeSeconds;
        int remainingSeconds = Math.max(totalAllowedSeconds - todayUsageSeconds, 0);

        return ShortformUsageResponse.builder()
                .todayUsageSeconds(todayUsageSeconds)
                .dailyLimitSeconds(dailyLimitSeconds)
                .extraTimeSeconds(extraTimeSeconds)
                .totalAllowedSeconds(totalAllowedSeconds)
                .remainingSeconds(remainingSeconds)
                .status(calculateStatus(todayUsageSeconds, totalAllowedSeconds))
                .build();
    }

    // 로그인한 사용자의 오늘 숏폼 누적 사용 시간, 남은 시간, 제한 상태를 조회한다.
    public ShortformTodayResponse getTodayUsage(Long userId) {
        User user = getUser(userId);

        LocalDate today = LocalDate.now(KOREA_ZONE);
        DailyShortform dailyShortform = getOrCreateDailyShortform(user, today);

        int todayUsageSeconds = dailyShortform.getTotalUsageSeconds();
        int dailyLimitSeconds = user.getDailyLimitSeconds();
        int extraTimeSeconds = dailyShortform.getExtraTimeSeconds();
        int totalAllowedSeconds = dailyLimitSeconds + extraTimeSeconds;
        int remainingSeconds = Math.max(totalAllowedSeconds - todayUsageSeconds, 0);

        return ShortformTodayResponse.builder()
                .todayUsageSeconds(todayUsageSeconds)
                .dailyLimitSeconds(dailyLimitSeconds)
                .extraTimeSeconds(extraTimeSeconds)
                .totalAllowedSeconds(totalAllowedSeconds)
                .remainingSeconds(remainingSeconds)
                .status(calculateStatus(todayUsageSeconds, totalAllowedSeconds))
                .build();
    }

    // 로그인한 사용자의 하루 숏폼 제한 시간을 조회한다.
    public ShortformLimitResponse getLimit(Long userId) {
        User user = getUser(userId);

        return ShortformLimitResponse.builder()
                .dailyLimitSeconds(user.getDailyLimitSeconds())
                .build();
    }

    // 로그인한 사용자의 하루 숏폼 제한 시간을 변경한다.
    @Transactional
    public ShortformLimitResponse updateLimit(Long userId, ShortformLimitUpdateRequest request) {
        User user = getUser(userId);

        user.updateDailyLimit(request.getDailyLimitSeconds());

        return ShortformLimitResponse.builder()
                .dailyLimitSeconds(user.getDailyLimitSeconds())
                .build();
    }

    // 사용자 PK로 User 엔티티를 조회한다.
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 사용자의 특정 날짜 일별 숏폼 요약 정보를 조회하고, 없으면 새로 생성한다.
    private DailyShortform getOrCreateDailyShortform(User user, LocalDate usageDate) {
        return dailyShortformRepository.findByUserAndUsageDate(user, usageDate)
                .orElseGet(() -> dailyShortformRepository.save(new DailyShortform(user, usageDate)));
    }

    // 오늘 누적 사용 시간이 총 허용 시간 이상이면 제한 초과 상태로 판단한다.
    private ShortformUsageStatus calculateStatus(int todayUsageSeconds, int totalAllowedSeconds) {
        if (todayUsageSeconds >= totalAllowedSeconds) {
            return ShortformUsageStatus.LIMIT_EXCEEDED;
        }

        return ShortformUsageStatus.AVAILABLE;
    }

    // 숏폼 사용 기록 요청값의 필수값, 시간 순서, 사용 시간 범위를 검증한다.
    private void validateUsageRequest(ShortformUsageRequest request) {
        if (request.getPlatform() == null) {
            throw new IllegalArgumentException("platform은 필수입니다.");
        }

        if (request.getStartedAt() == null || request.getEndedAt() == null) {
            throw new IllegalArgumentException("startedAt, endedAt은 필수입니다.");
        }

        if (!request.getStartedAt().isBefore(request.getEndedAt())) {
            throw new IllegalArgumentException("startedAt은 endedAt보다 이전이어야 합니다.");
        }

        if (request.getDurationSeconds() <= 0) {
            throw new IllegalArgumentException("durationSeconds는 1초 이상이어야 합니다.");
        }

        if (request.getDurationSeconds() > 1800) {
            throw new IllegalArgumentException("한 번에 기록할 수 있는 사용 시간은 최대 30분입니다.");
        }

        long realDuration = Duration.between(
                request.getStartedAt(),
                request.getEndedAt()
        ).getSeconds();

        if (Math.abs(realDuration - request.getDurationSeconds()) > 10) {
            throw new IllegalArgumentException("startedAt, endedAt, durationSeconds 값이 일치하지 않습니다.");
        }
    }
}