package org.example.cowmatchingbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.example.cowmatchingbe.domain.roulette.Prize;
import org.example.cowmatchingbe.domain.roulette.RouletteSpin;
import org.example.cowmatchingbe.dto.roulette.*;
import org.example.cowmatchingbe.service.RouletteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roulette")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {
        "http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"
}, allowCredentials = "true")
public class RouletteController {

    private final RouletteService rouletteService;

    // ---------- 1) 후보 경품 목록 ----------
    @GetMapping("/prizes")
    public ResponseEntity<?> getPrizes() {
        try {
            List<Prize> prizes = safeList(rouletteService.getActivePrizes());
            log.info("[GET /prizes] loaded: size={}", prizes.size());

            // DTO 변환 중 NPE 방어: 실패 레코드는 스킵하고 로그만 남김
            List<PrizeDto> body = prizes.stream()
                    .map(p -> {
                        try {
                            return PrizeDto.from(p);
                        } catch (Exception ex) {
                            log.error("[/prizes] DTO mapping failed for prize id={}", safeId(p), ex);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            log.info("[GET /prizes] dto size={}", body.size());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.error("[/prizes] unexpected error", e);
            return apiError(HttpStatus.INTERNAL_SERVER_ERROR, "경품 목록을 불러오지 못했습니다.");
        }
    }

    // ---------- 2) 남은 시도 ----------
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable Long userId) {
        try {
            int left = rouletteService.getAttemptsLeft(userId);
            return ResponseEntity.ok(new RouletteStatusDto(userId, left));
        } catch (Exception e) {
            log.error("[/status/{}] error", userId, e);
            return apiError(HttpStatus.INTERNAL_SERVER_ERROR, "남은 기회 조회에 실패했습니다.");
        }
    }

    // ---------- 3) 스핀 실행 ----------
    @PostMapping("/spin")
    public ResponseEntity<?> spin(@Valid @RequestBody SpinRequest req,
                                  @RequestHeader(value = "X-Forwarded-For", required = false) String xff,
                                  @RequestHeader(value = "User-Agent", required = false) String ua) {
        try {
            // 3-1) 남은 기회 체크
            int leftBefore = rouletteService.getAttemptsLeft(req.userId());
            if (leftBefore <= 0) {
                return apiError(HttpStatus.BAD_REQUEST, "더 이상 시도할 수 없습니다.");
            }

            // 3-2) 스핀 시도
            boolean won = rouletteService.spin(req.userId(), req.prizeId());

            // 3-3) 최신 남은 기회 & 당첨명
            int leftAfter = rouletteService.getAttemptsLeft(req.userId());
            String prizeName = null;
            if (won && req.prizeId() != null) {
                try {
                    Prize p = (Prize) rouletteService.getPrizes(); // 서비스에 단건 조회가 없으면 구현해줘
                    prizeName = (p != null ? p.getName() : null);
                } catch (Exception ignore) {
                    // prizeName 없으면 null로 둠
                }
            }

            // 3-4) 응답
            SpinResponse body = won
                    ? SpinResponse.winner(req.prizeId(), prizeName, null, leftAfter)
                    : SpinResponse.loser(leftAfter);

            log.info("[/spin] user={} result={} left={}", req.userId(), (won ? "WIN" : "LOSE"), leftAfter);
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException iae) {
            // 유효하지 않은 prizeId 등
            log.warn("[/spin] bad request: {}", iae.getMessage());
            return apiError(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (Exception e) {
            log.error("[/spin] unexpected error", e);
            return apiError(HttpStatus.INTERNAL_SERVER_ERROR, "스핀 처리에 실패했습니다.");
        }
    }

    // ---------- 4) 내 스핀 내역 ----------
    @GetMapping("/spins/{userId}")
    public ResponseEntity<?> getSpinHistory(@PathVariable Long userId) {
        try {
            List<RouletteSpin> spins = safeList(rouletteService.getUserSpins(userId));
            List<SpinHistoryDto> body = spins.stream()
                    .map(s -> {
                        try {
                            return SpinHistoryDto.from(s);
                        } catch (Exception ex) {
                            log.error("[/spins] DTO mapping failed for spin id={}", safeSpinId(s), ex);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.error("[/spins/{}] error", userId, e);
            return apiError(HttpStatus.INTERNAL_SERVER_ERROR, "스핀 내역 조회에 실패했습니다.");
        }
    }

    // ---------- 5) 글로벌 경품 재고 ----------
    @GetMapping("/prizes/stocks")
    public ResponseEntity<?> getPrizeStocks() {
        try {
            List<Prize> prizes = safeList(rouletteService.getActivePrizes());
            List<PrizeStockDto> body = prizes.stream()
                    .map(p -> {
                        try {
                            return PrizeStockDto.from(p);
                        } catch (Exception ex) {
                            log.error("[/prizes/stocks] DTO mapping failed for prize id={}", safeId(p), ex);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.error("[/prizes/stocks] error", e);
            return apiError(HttpStatus.INTERNAL_SERVER_ERROR, "재고 조회에 실패했습니다.");
        }
    }

    // ---------- 공통 유틸 ----------
    private static <T> List<T> safeList(List<T> in) {
        return in == null ? Collections.emptyList() : in;
    }

    private static Object safeId(Prize p) {
        try { return p.getId(); } catch (Exception e) { return "unknown"; }
    }
    private static Object safeSpinId(RouletteSpin s) {
        try { return s.getId(); } catch (Exception e) { return "unknown"; }
    }

    private ResponseEntity<ApiError> apiError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now().toString(), status.value(), message));
    }

    @Value
    static class ApiError {
        String timestamp;
        int status;
        String message;
    }
}