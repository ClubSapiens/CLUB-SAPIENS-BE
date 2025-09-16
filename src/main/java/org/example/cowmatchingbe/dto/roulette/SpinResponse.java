package org.example.cowmatchingbe.dto.roulette;

// 스핀 결과를 프론트가 알기 쉽게
public record SpinResponse(
        boolean success,     // 성공/실패
        Long prizeId,        // 당첨 경품 id (꽝이면 null)
        String prizeCode,    // 예: "STARBUCKS_AMERICANO" (꽝이면 "LOSER" or null)
        String prizeName,    // 예: "스타벅스 아메리카노" (꽝이면 "꽝")
        Integer attemptsLeft // 이번 스핀 이후 남은 시도 횟수
) {
    public static SpinResponse loser(int attemptsLeft) {
        return new SpinResponse(true, null, "LOSER", "꽝", attemptsLeft);
    }
}