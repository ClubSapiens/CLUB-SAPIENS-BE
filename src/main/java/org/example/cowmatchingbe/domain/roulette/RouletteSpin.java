package org.example.cowmatchingbe.domain.roulette;

import jakarta.persistence.*;
import lombok.*;
import org.example.cowmatchingbe.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name="roulette_spins")
@Getter @Setter @NoArgsConstructor
public class RouletteSpin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;    // FK(users.id)

    @Column(name = "prize_id") private Long prizeId; // 꽝이면 null

    @Column(nullable = false)
    private Boolean won = false;  // true=당첨, false=꽝

    private String ip;
    private String ua;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public RouletteSpin(Long userId, Long prizeId, Boolean won) {
        this.userId = userId;
        this.prizeId = prizeId;
        this.won = won;
        this.createdAt = LocalDateTime.now();
    }

}