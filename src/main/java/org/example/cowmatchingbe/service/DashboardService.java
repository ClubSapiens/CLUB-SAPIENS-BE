// src/main/java/org/example/cowmatchingbe/service/DashboardService.java
package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.repository.MatchRepository;
import org.example.cowmatchingbe.repository.ProfileRepository;
import org.example.cowmatchingbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final MatchRepository matchRepository;

    /** 대시보드 요약 지표 */
    public SummaryMetrics getSummary() {
        long totalUsers        = userRepository.count();
        long totalProfiles     = profileRepository.count();
        long totalMatches      = matchRepository.countAllMatches();
        long distinctMatchers  = matchRepository.countDistinctMatchers();
        long maleUsers         = userRepository.countByGender("male");
        long femaleUsers       = userRepository.countByGender("female");

        return new SummaryMetrics(
                totalUsers,
                totalProfiles,
                totalMatches,
                distinctMatchers,
                maleUsers,
                femaleUsers
        );
    }

    /** 프론트로 그대로 직렬화되는 DTO */
    public record SummaryMetrics(
            long totalUsers,
            long totalProfiles,
            long totalMatches,
            long distinctMatchers,
            long maleUsers,
            long femaleUsers
    ) {}
}