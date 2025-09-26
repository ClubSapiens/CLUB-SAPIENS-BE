package org.example.cowmatchingbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.roulette.Prize;
import org.example.cowmatchingbe.domain.roulette.RouletteSpin;
import org.example.cowmatchingbe.dto.roulette.*;
import org.example.cowmatchingbe.service.RouletteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roulette")
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    /** 1) 후보 경품 목록 조회 */
    @GetMapping("/prizes")
    public ResponseEntity<List<PrizeDto>> getPrizes() {
        List<Prize> prizes = rouletteService.getActivePrizes();
        List<PrizeDto> body = prizes.stream().map(PrizeDto::from).toList();
        return ResponseEntity.ok(body);
    }

    /** 2) 남은 시도 횟수 조회 */
    @GetMapping("/status/{userId}")
    public ResponseEntity<RouletteStatusDto> getStatus(@PathVariable Long userId) {
        int left = rouletteService.getAttemptsLeft(userId);
        return ResponseEntity.ok(new RouletteStatusDto(userId, left));
    }

    /** 3) 스핀 실행 */
    @PostMapping("/spin")
    public ResponseEntity<SpinResponse> spin(@Valid @RequestBody SpinRequest req,
                                             @RequestHeader(value = "X-Forwarded-For", required = false) String xff,
                                             @RequestHeader(value = "User-Agent", required = false) String ua) {
        boolean ok = rouletteService.spin(req.userId(), req.prizeId());
        int left = rouletteService.getAttemptsLeft(req.userId());

        return ResponseEntity.ok(
                SpinResponse.winner(req.prizeId(), null, null, left)
        );
    }

    /**  4) 유저의 스핀 내역 조회 */
    @GetMapping("/spins/{userId}")
    public ResponseEntity<List<SpinHistoryDto>> getSpinHistory(@PathVariable Long userId) {
        List<RouletteSpin> spins = rouletteService.getUserSpins(userId);
        List<SpinHistoryDto> body = spins.stream().map(SpinHistoryDto::from).toList();
        return ResponseEntity.ok(body);
    }

    /** 5) 글로벌 경품 재고 현황 조회 */
    @GetMapping("/prizes/stocks")
    public ResponseEntity<List<PrizeStockDto>> getPrizeStocks() {
        List<Prize> prizes = rouletteService.getActivePrizes();
        List<PrizeStockDto> body = prizes.stream().map(PrizeStockDto::from).toList();
        return ResponseEntity.ok(body);
    }
}