package org.example.cowmatchingbe.repository;

import org.example.cowmatchingbe.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndTargetId(Long userId, Long targetId);
    @Query("select count(m) from Match m")
    long countAllMatches();

    @Query("select count(distinct m.userId) from Match m")
    long countDistinctMatchers();  // 매칭을 누른 고유 사용자 수
}