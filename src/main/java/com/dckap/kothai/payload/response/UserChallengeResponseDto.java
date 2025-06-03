package com.dckap.kothai.payload.response;

import lombok.Data;
import software.amazon.awssdk.services.route53.endpoints.internal.Value;

@Data
public class UserChallengeResponseDto {
    private Long id;
    private ChallengeResponseDto challenge;
    private UserResponseDto user;
    private String status;
    private String typedContent;
    private Integer timeTaken;
    private double accuracy;
    private double speed;
    private String batchName;
}
