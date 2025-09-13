-- V3__roulette.sql
-- 목적: 룰렛 기능 스키마 (prizes / roulette_status / roulette_spins) 및 샘플 시드
-- 안전하게 재실행 가능하도록 설계 (IF NOT EXISTS, 조건부 INSERT)

-- 1) 상품 마스터
CREATE TABLE IF NOT EXISTS prizes (
                                      id             BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      code           VARCHAR(64) UNIQUE,                         -- 운영 편의용(옵션). 예: STARBUCKS_AMERICANO
    name           VARCHAR(100) NOT NULL,                      -- 표기 이름
    description    VARCHAR(255),
    weight         INT NOT NULL DEFAULT 1,                     -- ✅ 확률 가중치 (클수록 잘 나옴)
    stock          INT NOT NULL DEFAULT 0,                     -- 남은 재고
    color          VARCHAR(16) DEFAULT '#22c55e',              -- UI 색상(hex)
    display_order  INT NOT NULL DEFAULT 0,                     -- UI 표기 순서
    is_active      TINYINT(1) NOT NULL DEFAULT 1,              -- 비활성화용 플래그
    is_loser       TINYINT(1) NOT NULL DEFAULT 0,              -- ✅ '꽝' 여부 (재고와 무관하게 추첨 풀 포함시킬 때 사용)
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- 2) 유저별 스핀 상태 (초기 2회)
CREATE TABLE IF NOT EXISTS roulette_status (
                                               user_id        BIGINT PRIMARY KEY,                         -- 유저당 1행
                                               attempts_left  INT NOT NULL DEFAULT 2,                     -- ✅ 초기 스핀 2회
                                               last_spin_at   DATETIME NULL,
                                               created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                               updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                               CONSTRAINT fk_rs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- 3) 스핀 로그 (히스토리)
CREATE TABLE IF NOT EXISTS roulette_spins (
                                              id             BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              user_id        BIGINT NOT NULL,
                                              prize_id       BIGINT NULL,                                -- 꽝이면 NULL 가능
                                              won            TINYINT(1) NOT NULL DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rsl_user  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_rsl_prize FOREIGN KEY (prize_id) REFERENCES prizes(id) ON DELETE SET NULL
    );

-- 조회 성능 최적화용 인덱스
-- 조회 성능 최적화용 인덱스 (있으면 스킵)
SET @idx_exists := (
  SELECT COUNT(1) FROM information_schema.statistics
  WHERE table_schema = DATABASE()
    AND table_name = 'roulette_spins'
    AND index_name = 'idx_rsl_user_created'
);
SET @add_idx_sql := IF(@idx_exists = 0,
  'ALTER TABLE roulette_spins ADD INDEX idx_rsl_user_created (user_id, created_at);',
  'SELECT 1;'
);
PREPARE stmt FROM @add_idx_sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ===========================
-- (선택) 초기 샘플 데이터
-- ===========================

-- 꽝 (is_loser=1, 재고 의미 없음)
INSERT INTO prizes (code, name, description, weight, stock, color, display_order, is_active, is_loser)
SELECT 'LOSER', '꽝', '다음 기회에…', 70, 999999, '#f87171', 999, 1, 1
    WHERE NOT EXISTS (SELECT 1 FROM prizes WHERE code = 'LOSER');

-- 예시 경품 1 (스타벅스 아메리카노)
INSERT INTO prizes (code, name, description, weight, stock, color, display_order, is_active, is_loser)
SELECT 'STARBUCKS_AMERICANO', '스타벅스 아메리카노', '기프티콘', 10, 5, '#10b981', 1, 1, 0
    WHERE NOT EXISTS (SELECT 1 FROM prizes WHERE code = 'STARBUCKS_AMERICANO');

-- 예시 경품 2 (CU 3천원 쿠폰)
INSERT INTO prizes (code, name, description, weight, stock, color, display_order, is_active, is_loser)
SELECT 'CU_3000', 'CU 3천원', '편의점 쿠폰', 20, 10, '#60a5fa', 2, 1, 0
    WHERE NOT EXISTS (SELECT 1 FROM prizes WHERE code = 'CU_3000');