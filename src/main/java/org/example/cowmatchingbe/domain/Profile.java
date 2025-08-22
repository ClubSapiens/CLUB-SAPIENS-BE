package org.example.cowmatchingbe.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Profile {
    @Id @Column(name="user_id")
    private Long userId; // PK=FK (단순히 ID로만 매핑)

    @Column(nullable=false, length=50) private String nickname;
    @Column(nullable=false, length=10) private String mbti;
    @Column(columnDefinition="text")   private String description;
    @Column(name="insta_profile", length=100) private String instaProfile;

    @Column(name="created_at", insertable=false, updatable=false)
    private LocalDateTime createdAt;
}