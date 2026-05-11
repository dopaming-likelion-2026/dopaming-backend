package com.dopaming.backend.api.shortform.dto.request;

import com.dopaming.backend.api.shortform.entity.ShortformPlatform;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShortformUsageRequest {

    private ShortformPlatform platform;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private int durationSeconds;
}