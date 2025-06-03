package com.dckap.kothai.payload.response;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import com.dckap.kothai.type.ChallengeType;

@Data
public class ChallengeResponseDto {
    private Long challengeId;
    private String title;
    private String description;
    private ChallengeType type;
    private Integer level;
    private String content;
    private Integer timeLimit;
    private double accuracy;
    private double speed;
    private Boolean isActive;
    private Boolean isBlocked;
    private Integer winXp;
    private Integer loseXp;
}
