package org.example.cowmatchingbe.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="matches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Match {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id",   nullable=false) private Long userId;
    @Column(name="target_id", nullable=false) private Long targetId;

    @Column(name="created_at", insertable=false, updatable=false)
    private LocalDateTime createdAt;
}