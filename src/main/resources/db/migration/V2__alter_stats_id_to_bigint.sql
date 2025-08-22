-- V2: stats.id 를 INT -> BIGINT 로 변경
-- MySQL 8 / MariaDB 호환

ALTER TABLE stats
    MODIFY COLUMN id BIGINT NOT NULL;

-- (PK는 그대로 유지됩니다. 기존에 PRIMARY KEY였으면 유지됨)