package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.cowmatchingbe.domain.roulette.Prize;
import org.example.cowmatchingbe.domain.roulette.RouletteSpin;
import org.example.cowmatchingbe.domain.roulette.RouletteStatus;
import org.example.cowmatchingbe.repository.Roulette.PrizeRepository;
import org.example.cowmatchingbe.repository.Roulette.RouletteSpinRepository;
import org.example.cowmatchingbe.repository.Roulette.RouletteStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final PrizeRepository prizeRepository;
    private final RouletteStatusRepository statusRepository;
    private final RouletteSpinRepository spinRepository;

    /** 룰렛 후보 상품 조회 */
    @Transactional(readOnly = true)
    public List<Prize> getActivePrizes() {
        return prizeRepository.findActiveCandidates();
    }

    /** 유저의 현재 시도 횟수 조회 */
    @Transactional(readOnly = true)
    public int getAttemptsLeft(Long userId) {
        return statusRepository.findById(userId)
                .map(RouletteStatus::getAttemptsLeft)
                .orElse(2); // 없으면 기본값 2
    }

    /** 스핀 실행 (간단 버전: 재고만 차감, 로그 저장) */
    @Transactional
    public boolean spin(Long userId, Long prizeId) {
        // 1. 남은 시도 차감
        RouletteStatus status = statusRepository.findById(userId)
                .orElse(new RouletteStatus(userId, 2));
        if (status.getAttemptsLeft() <= 0) {
            throw new IllegalStateException("더 이상 시도할 수 없습니다.");
        }
        status.setAttemptsLeft(status.getAttemptsLeft() - 1);
        statusRepository.save(status);

        // 2. 재고 차감
        int updated = prizeRepository.decrementStock(prizeId);
        if (updated == 0) {
            throw new IllegalStateException("재고가 없습니다.");
        }

        // 3. 로그 저장
        RouletteSpin spin = new RouletteSpin(userId, prizeId, true);
        spinRepository.save(spin);

        return true;
    }
}