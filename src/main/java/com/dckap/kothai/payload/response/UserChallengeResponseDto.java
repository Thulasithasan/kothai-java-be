package com.dckap.kothai.payload.response;

import lombok.Data;

@Data
public class UserChallengeResponseDto {
    private Long id;
    private String challengeId;
    private String userId;
    private String status;
    private String typedContent;
    private Integer timeTaken;
    private Integer accuracy;
    private Integer speed;
}
