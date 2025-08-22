
package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.Match;
import org.example.cowmatchingbe.domain.Stats;
import org.example.cowmatchingbe.repository.MatchRepository;
import org.example.cowmatchingbe.repository.StatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final MatchRepository matchRepository;
    private final StatsRepository statsRepository;
    private final UserService userService;
    private final StatsService statsService; //유저별 통계

    /** 매칭 생성: 중복 방지 + 본인 check_num 증가 + stats.match_count 증가(트랜잭션) */
    @Transactional
    public Match create(Long userId, Long targetId) {
        if (matchRepository.existsByUserIdAndTargetId(userId, targetId)) {
            // 이미 매칭한 상대면 null 반환(또는 예외 던지기)
            return null;
        }
        Match saved = matchRepository.save(
                Match.builder().userId(userId).targetId(targetId).build()
        );

        // 부수효과들(같은 트랜잭션에서)
        userService.increaseCheckNum(userId);

        statsService.incrementMatchCount((userId));

        return saved;
    }

    /** 내가 매칭한 목록 */
    public List<Match> my(Long userId) {
        return matchRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}