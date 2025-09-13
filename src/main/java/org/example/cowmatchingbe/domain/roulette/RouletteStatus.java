package org.example.cowmatchingbe.domain.roulette;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;

// domain/roulette/RouletteStatus.java
@Entity
@Table(name="roulette_status")
@Getter
@Setter
@NoArgsConstructor
public class RouletteStatus {
    @Id
    private Long userId;             // PK = FK(users.id)
    @Column(nullable=false) private Integer attemptsLeft = 2;
    private LocalDateTime lastSpinAt;
    @Column(insertable=false, updatable=false) private Timestamp createdAt;
    @Column(insertable=false, updatable=false) private Timestamp updatedAt;

    public RouletteStatus(Long userId, Integer attemptsLeft, LocalDateTime lastSpinAt) {
        this.userId = userId;
        this.attemptsLeft = attemptsLeft;
        this.lastSpinAt = lastSpinAt;
    }
}