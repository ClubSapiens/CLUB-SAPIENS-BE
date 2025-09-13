package org.example.cowmatchingbe.repository.Roulette;

import jakarta.persistence.LockModeType;
import org.example.cowmatchingbe.domain.roulette.RouletteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// repository/roulette/RouletteStatusRepository.java
public interface RouletteStatusRepository extends JpaRepository<RouletteStatus, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // SELECT ... FOR UPDATE
    @Query("select r from RouletteStatus r where r.userId = :userId")
    Optional<RouletteStatus> findForUpdate(@Param("userId") Long userId);
}