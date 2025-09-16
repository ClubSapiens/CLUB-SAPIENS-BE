package org.example.cowmatchingbe.dto.roulette;

// 모든 에러 응답을 이 포맷으로 (프론트 처리 편해짐)
public record ApiErrorResponse(
        String code,     // 예: "OUT_OF_STOCK", "NO_ATTEMPTS"
        String message   // 사용자에게 보여줄 메시지
) {}