// dto/roulette/SpinHistoryDto.java
package org.example.cowmatchingbe.dto.roulette;

import org.example.cowmatchingbe.domain.roulette.RouletteSpin;

import java.time.LocalDateTime;

public record SpinHistoryDto(
        Long prizeId,
        Boolean won,
        LocalDateTime createdAt
) {
    public static SpinHistoryDto from(RouletteSpin spin) {
        return new SpinHistoryDto(
                spin.getPrizeId(),
                spin.getWon(),
                spin.getCreatedAt()
        );
    }
}