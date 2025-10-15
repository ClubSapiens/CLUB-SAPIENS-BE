package org.example.cowmatchingbe.dto.roulette;

import org.example.cowmatchingbe.domain.roulette.Prize;

// 응답에서 필요한 필드만 뽑아 쓰는 DTO (엔티티 그대로 노출 X)
public record PrizeDto(
        Long id,
        String code,
        String name,
        String description,
        Integer weight,
        Integer stock,
        String color,
        Integer displayOrder,
        Boolean active,
        Boolean loser
) {
    // 엔티티 → DTO로 변환하는 팩토리 메서드
    public static PrizeDto from(Prize p) {
        return new PrizeDto(
                p.getId(),
                p.getCode(),
                p.getName(),
                p.getDescription(),
                p.getWeight(),
                p.getStock(),
                p.getColor(),
                p.getDisplayOrder(),
                p.isActive(),
                p.isLoser()
        );
    }
}