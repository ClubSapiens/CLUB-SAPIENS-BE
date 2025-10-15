-- V4__roulette_refactor.sql
-- 목적: 룰렛 관련 테이블(prizes, roulette_status, roulette_spins) 재정의
-- 기존 V3 구조를 대체

-- 🔸 안전하게 제거 후 재생성 (개발 DB 한정)
DROP TABLE IF EXISTS roulette_spins;
DROP TABLE IF EXISTS roulette_status;
DROP TABLE IF EXISTS prizes;

CREATE TABLE prizes (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        code VARCHAR(64) UNIQUE,
                        name VARCHAR(100) NOT NULL,
                        description VARCHAR(255),
                        weight INT NOT NULL DEFAULT 1,
                        stock INT NOT NULL DEFAULT 0,
                        color VARCHAR(16) DEFAULT '#22c55e',
                        display_order INT NOT NULL DEFAULT 0,
                        is_active TINYINT(1) NOT NULL DEFAULT 1,
                        is_loser TINYINT(1) NOT NULL DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE roulette_status (
                                 user_id BIGINT PRIMARY KEY,
                                 attempts_left INT NOT NULL DEFAULT 2,
                                 last_spin_at DATETIME NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_rs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE roulette_spins (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                prize_id BIGINT NULL,
                                won TINYINT(1) NOT NULL DEFAULT 0,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_rsl_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                CONSTRAINT fk_rsl_prize FOREIGN KEY (prize_id) REFERENCES prizes(id) ON DELETE SET NULL
);

CREATE INDEX idx_rsl_user_created ON roulette_spins (user_id, created_at);

-- 샘플 데이터 삽입
INSERT INTO prizes (code, name, description, weight, stock, color, display_order, is_active, is_loser)
SELECT 'LOSER', '꽝', '다음 기회에…', 70, 999999, '#f87171', 999, 1, 1
    WHERE NOT EXISTS (SELECT 1 FROM prizes WHERE code = 'LOSER');

INSERT INTO prizes (code, name, description, weight, stock, color, display_order, is_active, is_loser)
SELECT 'STARBUCKS_AMERICANO', '스타벅스 아메리카노', '기프티콘', 10, 5, '#10b981', 1, 1, 0
    WHERE NOT EXISTS (SELECT 1 FROM prizes WHERE code = 'STARBUCKS_AMERICANO');

INSERT INTO prizes (code, name, description, weight, stock, color, display_order, is_active, is_loser)
SELECT 'CU_3000', 'CU 3천원', '편의점 쿠폰', 20, 10, '#60a5fa', 2, 1, 0
    WHERE NOT EXISTS (SELECT 1 FROM prizes WHERE code = 'CU_3000');