// dto/roulette/PrizeStockDto.java
package org.example.cowmatchingbe.dto.roulette;

import org.example.cowmatchingbe.domain.roulette.Prize;

public record PrizeStockDto(
        Long id,
        String name,
        Integer stock
) {
    public static PrizeStockDto from(Prize prize) {
        return new PrizeStockDto(
                prize.getId(),
                prize.getName(),
                prize.getStock()
        );
    }
}