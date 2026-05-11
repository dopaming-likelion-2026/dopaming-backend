package com.dopaming.backend.api.shortform.entity;

import com.dopaming.backend.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DailyShortform: 날짜별 누적 사용 시간 저장

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "daily_shortform",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "usage_date"})
        }
)
public class DailyShortform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 한국 기준 사용 날짜
    @Column(nullable = false)
    private LocalDate usageDate;

    // 오늘 누적 숏폼 사용 시간, 초 단위
    @Column(nullable = false)
    private int totalUsageSeconds;

    // 퀴즈 정답으로 추가 허용된 시간, 초 단위
    @Column(nullable = false)
    private int extraTimeSeconds;

    public DailyShortform(User user, LocalDate usageDate) {
        this.user = user;
        this.usageDate = usageDate;
        this.totalUsageSeconds = 0;
        this.extraTimeSeconds = 0;
    }

    public void addUsageSeconds(int durationSeconds) {
        this.totalUsageSeconds += durationSeconds;
    }

    public void addExtraTimeSeconds(int seconds) {
        this.extraTimeSeconds += seconds;
    }
}