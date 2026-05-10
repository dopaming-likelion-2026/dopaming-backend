package com.dopaming.backend.api.shortform.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortformLimitResponse {

    private int dailyLimit;

    private int dailyLimitMinutes;
}