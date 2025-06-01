package com.dckap.kothai.payload.request;

import com.dckap.kothai.type.ChallengeType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChallengeRequestDto {

    @NotBlank(message = "Title is required")
    @Schema(description = "Title of the challenge", example = "Challenge 1")
    private String title;

    @NotBlank(message = "Description is required")
    @Schema(description = "Description of the challenge", example = "Your first challenge")
    private String description;

    @NotBlank(message = "Type is required")
    @Schema(description = "Type of the challenge", example = "LEVEL")
    private ChallengeType type;

    @NotBlank(message = "Level is required")
    @Schema(description = "Level of the challenge", example = "1")
    private Integer level;

    @NotBlank(message = "Content is required")
    @Schema(description = "Content of the challenge", example = "Hello, how are you?")
    private String content;

    @NotBlank(message = "Time limit is required")
    @Schema(description = "Time limit of the challenge", example = "10")
    private Integer timeLimit;

    @NotBlank(message = "Accuracy is required")
    @Schema(description = "Accuracy of the challenge. (0-100)%", example = "90")
    private Integer accuracy;

    @NotBlank(message = "Speed is required")
    @Schema(description = "Speed of the challenge in words per minute(WPM)", example = "25")
    private Integer speed;
}
