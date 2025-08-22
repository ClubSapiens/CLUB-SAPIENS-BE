CREATE TABLE IF NOT EXISTS users (
                                     id        BIGINT       PRIMARY KEY,
                                     name      VARCHAR(50)  NOT NULL,
    gender    VARCHAR(10)  NULL,
    check_num INT          NOT NULL DEFAULT 0,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS profiles (
                                        user_id      BIGINT      PRIMARY KEY,
                                        nickname     VARCHAR(50) NOT NULL,
    mbti         VARCHAR(10) NOT NULL,
    description  TEXT        NULL,
    insta_profile VARCHAR(100) NULL,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS matches (
                                       id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       user_id    BIGINT NOT NULL,
                                       target_id  BIGINT NOT NULL,
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       INDEX idx_user (user_id),
    INDEX idx_target (target_id),
    CONSTRAINT fk_match_user   FOREIGN KEY (user_id)   REFERENCES users(id),
    CONSTRAINT fk_match_target FOREIGN KEY (target_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS stats (
                                     id          INT PRIMARY KEY,
                                     match_count INT NOT NULL DEFAULT 0
);

INSERT IGNORE INTO stats(id, match_count) VALUES (1, 0);