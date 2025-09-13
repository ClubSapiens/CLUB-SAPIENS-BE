package org.example.cowmatchingbe.repository.Roulette;


import org.example.cowmatchingbe.domain.roulette.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrizeRepository extends JpaRepository<Prize, Long> {

    @Query("""
              select p from Prize p
              where p.isActive = true and (p.isLoser = true or p.stock > 0)
              order by p.displayOrder asc, p.id asc
            """)
    List<Prize> findActiveCandidates();

    @Modifying
    @Query("update Prize p set p.stock = p.stock - 1 where p.id = :id and p.stock > 0")
    int decrementStock(@Param("id") Long id); // 원자적 차감
}