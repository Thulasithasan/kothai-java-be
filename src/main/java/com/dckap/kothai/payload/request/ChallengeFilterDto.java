package com.dckap.kothai.payload.request;

import com.dckap.kothai.type.ChallengeType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data   
public class ChallengeFilterDto {

    @Schema(description = "Type of the challenge", example = "LEVEL")
    private ChallengeType type;

    @Schema(description = "Is active of the challenge", example = "true")   
    private Boolean isActive;

    @Schema(description = "Is pagination required", example = "true")
    private Boolean isPaginationRequired;

    @Schema(description = "Page number", example = "1")
    private Integer pageNumber;

    @Schema(description = "Page size", example = "10")
    private Integer pageSize;   
}
