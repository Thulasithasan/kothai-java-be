package com.dckap.kothai.model;

import com.dckap.kothai.type.UserChallengeStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_challenges")
public class UserChallenge extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_challenge_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;


    @Column(name = "typed_content", columnDefinition = "TEXT")
    private String typedContent;

    @Column(name = "accuracy", nullable = false)
    private Integer accuracy;

    @Column(name = "speed", nullable = false)
    private Integer speed;

    @Column(name = "time_taken", nullable = false)
    private Integer timeTaken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)  
    private UserChallengeStatus status;
}
