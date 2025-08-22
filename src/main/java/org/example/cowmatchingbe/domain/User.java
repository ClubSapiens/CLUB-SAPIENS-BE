package org.example.cowmatchingbe.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id private Long id;

    @Column(nullable=false, length=50)
    private String name;

    @Column(length=10)
    private String gender;

    @Column(name="check_num", nullable=false)
    private Integer checkNum = 0;

    @Column(name="created_at", insertable=false, updatable=false)
    private LocalDateTime createdAt;
}