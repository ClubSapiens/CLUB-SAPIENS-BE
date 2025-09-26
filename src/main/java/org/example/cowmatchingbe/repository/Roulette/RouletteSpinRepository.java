package org.example.cowmatchingbe.repository.Roulette;

import org.example.cowmatchingbe.domain.roulette.RouletteSpin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouletteSpinRepository extends JpaRepository<RouletteSpin, Long> {
    List<RouletteSpin> findByUserIdOrderByCreatedAtDesc(Long userId);
}