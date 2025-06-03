package com.dckap.kothai.payload.response;

import lombok.Data;

@Data
public class BadgeResponseDto {
    private Long badgeId;
    private String name;
    private String description;
    private String criteriaType;
    private String criteriaOp;
    private double criteriaValue;
}
