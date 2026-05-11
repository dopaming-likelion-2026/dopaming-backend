package com.dopaming.backend.api.shortform.controller;

import com.dopaming.backend.api.shortform.dto.request.ShortformLimitUpdateRequest;
import com.dopaming.backend.api.shortform.dto.request.ShortformUsageRequest;
import com.dopaming.backend.api.shortform.service.ShortformService;
import com.dopaming.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shortform")
@RequiredArgsConstructor
public class ShortformController {

    private final ShortformService shortformService;

    // 숏폼 사용 시간을 저장하고, 오늘 누적 사용 시간과 제한 시간 초과 여부를 반환한다.
    @PostMapping("/usage")
    public ResponseEntity<?> recordUsage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ShortformUsageRequest request
    ) {
        Long userId = userDetails.getUserId(); // User 엔티티의 id

        var response = shortformService.recordUsage(userId, request);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "숏폼 사용 시간이 기록되었습니다.",
                "data", response
        ));
    }

    // 로그인한 사용자의 오늘 숏폼 누적 사용 시간, 남은 시간, 제한 상태를 조회한다.
    @GetMapping("/usage/today")
    public ResponseEntity<?> getTodayUsage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        var response = shortformService.getTodayUsage(userId);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "오늘 숏폼 사용 시간이 조회되었습니다.",
                "data", response
        ));
    }

    // 로그인한 사용자의 하루 숏폼 제한 시간을 조회한다.
    @GetMapping("/limit")
    public ResponseEntity<?> getLimit(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        var response = shortformService.getLimit(userId);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "하루 숏폼 제한 시간이 조회되었습니다.",
                "data", response
        ));
    }

    // 로그인한 사용자의 하루 숏폼 제한 시간을 변경한다.
    @PutMapping("/limit")
    public ResponseEntity<?> updateLimit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ShortformLimitUpdateRequest request
    ) {
        Long userId = userDetails.getUserId();

        var response = shortformService.updateLimit(userId, request);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "하루 숏폼 제한 시간이 변경되었습니다.",
                "data", response
        ));
    }
}