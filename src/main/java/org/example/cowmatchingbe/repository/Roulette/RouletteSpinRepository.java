package org.example.cowmatchingbe.repository.Roulette;

import org.example.cowmatchingbe.domain.roulette.RouletteSpin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouletteSpinRepository extends JpaRepository<RouletteSpin, Long> {}