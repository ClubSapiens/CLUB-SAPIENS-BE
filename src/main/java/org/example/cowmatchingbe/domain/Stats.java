package org.example.cowmatchingbe.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="stats")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Stats {
    @Id private Integer id;
    @Column(name="match_count", nullable=false) private Integer matchCount;
}