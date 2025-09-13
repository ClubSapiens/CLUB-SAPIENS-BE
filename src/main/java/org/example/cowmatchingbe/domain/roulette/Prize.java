package org.example.cowmatchingbe.domain.roulette;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;

@Entity @Table(name="prizes")
@Getter @Setter @NoArgsConstructor
public class Prize {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, length=64) private String code;
    @Column(nullable=false, length=100) private String name;
    @Column(length=255) private String description;

    @Column(nullable=false) private Integer weight = 1; // 가중치
    @Column(nullable=false) private Integer stock  = 0; // 재고
    @Column(length=16) private String color;
    @Column(nullable=false) private Integer displayOrder = 0;
    @Column(nullable=false) private Boolean isActive = true;
    @Column(nullable=false) private Boolean isLoser  = false;

    @Column(insertable=false, updatable=false) private Timestamp createdAt;
    @Column(insertable=false, updatable=false) private Timestamp updatedAt;
}