package org.example.cowmatchingbe.dto.roulette;

public record SpinResponse(
        boolean success,
        Long prizeId,
        String prizeCode,
        String prizeName,
        Integer attemptsLeft
) {
    // 꽝일 때
    public static SpinResponse loser(int attemptsLeft) {
        return new SpinResponse(true, null, "LOSER", "꽝", attemptsLeft);
    }

    // 당첨일 때
    public static SpinResponse winner(Long prizeId, String prizeCode, String prizeName, int attemptsLeft) {
        return new SpinResponse(true, prizeId, prizeCode, prizeName, attemptsLeft);
    }

    // 실패 케이스도 명확히 하고 싶으면
    public static SpinResponse fail(String reason, int attemptsLeft) {
        return new SpinResponse(false, null, reason, "실패", attemptsLeft);
    }
}