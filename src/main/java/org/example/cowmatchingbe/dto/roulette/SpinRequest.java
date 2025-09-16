package org.example.cowmatchingbe.dto.roulette;

import jakarta.validation.constraints.NotNull;

// POST /spin 의 요청 바디
public record SpinRequest(
        @NotNull Long userId,   // 누가 돌렸는지
        @NotNull Long prizeId   // 어떤 경품 후보를 선택했는지 (프론트가 선택)
) {}