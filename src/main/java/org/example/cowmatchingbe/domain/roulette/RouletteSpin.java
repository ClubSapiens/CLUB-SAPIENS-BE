package org.example.cowmatchingbe.domain.roulette;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
// domain/roulette/RouletteSpin.java
@Entity @Table(name="roulette_spins")
@Getter @Setter @NoArgsConstructor
public class RouletteSpin {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private Long userId;    // FK(users.id)
    private Long prizeId;                           // 꽝이면 null
    @Column(nullable=false) private Integer won = 0; // 1 당첨, 0 꽝
    private String ip;
    private String ua;

    @Column(nullable=false, updatable=false)
    private Instant createdAt = Instant.now();
}