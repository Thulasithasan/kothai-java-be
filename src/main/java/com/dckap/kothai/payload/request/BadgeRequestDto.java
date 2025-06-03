package com.dckap.kothai.payload.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class BadgeRequestDto {
    @NotBlank(message = "Name is required")
    @Schema(description = "Badge name", example = "Accuracy Ace")
    private String name;

    @NotBlank(message = "Description is required")
    @Schema(description = "Badge description", example = "Achieve 95% accuracy")
    private String description;

    @NotBlank(message = "Criteria type is required")
    @Schema(description = "Criteria type", example = "accuracy")
    private String criteriaType;

    @NotBlank(message = "Criteria operator is required")
    @Schema(description = "Criteria operator", example = "==")
    private String criteriaOp;

    @NotBlank(message = "Criteria value is required")
    @Schema(description = "Criteria value", example = "95")
    private double criteriaValue;
}
