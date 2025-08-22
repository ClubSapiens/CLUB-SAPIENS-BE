
package org.example.cowmatchingbe.repository;

import org.example.cowmatchingbe.domain.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stats, Long> {}