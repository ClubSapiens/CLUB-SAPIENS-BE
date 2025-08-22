package org.example.cowmatchingbe.repository;

import org.example.cowmatchingbe.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndTargetId(Long userId, Long targetId);
}