package com.dckap.kothai.model;

import com.dckap.kothai.type.ChallengeType;
import com.dckap.kothai.type.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "challenges")
public class Challenge extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id", nullable = false, updatable = false)
    private Long challengeId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChallengeType type;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "time_limit_seconds", nullable = false)
    private Integer timeLimit;

    @Column(name = "required_accuracy", nullable = false)
    private Integer accuracy;

    @Column(name = "required_speed_wpm", nullable = false)
    private Integer speed;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
