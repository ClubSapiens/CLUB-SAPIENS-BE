package org.example.cowmatchingbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.Stats;
import org.example.cowmatchingbe.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    /** 유저별 통계 조회 */
    @GetMapping("/{userId}")
    public ResponseEntity<Stats> get(@PathVariable Long userId) {
        return statsService.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** 유저별 match_count +1 */
    @PostMapping("/{userId}/increment-match")
    public ResponseEntity<Stats> increment(@PathVariable Long userId) {
        return ResponseEntity.ok(statsService.incrementMatchCount(userId));
    }
}