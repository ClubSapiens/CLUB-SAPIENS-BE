// src/main/java/org/example/cowmatchingbe/service/StatsService.java
package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.Stats;
import org.example.cowmatchingbe.repository.StatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;

    /** 특정 userId의 stats 가져오기 */
    @Transactional(readOnly = true)
    public Optional<Stats> getByUserId(Long userId) {
        return statsRepository.findById(userId);
        // 이제 intValue() 필요 없음
    }

    /** 특정 userId의 match_count 증가 */
    @Transactional
    public Stats incrementMatchCount(Long userId) {
        var stats = statsRepository.findById(userId)
                .orElseGet(() -> {
                    // 처음 유저의 stats 없으면 생성
                    Stats s = new Stats();
                    s.setId(userId);
                    s.setMatchCount(0);
                    return s;
                });

        stats.setMatchCount(stats.getMatchCount() + 1);
        return statsRepository.save(stats);
    }
}