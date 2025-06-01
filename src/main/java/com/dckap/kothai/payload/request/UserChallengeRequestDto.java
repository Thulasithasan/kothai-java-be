package com.dckap.kothai.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserChallengeRequestDto {

    @Schema(description = "Challenge ID", example = "1")
    @NotNull(message = "Challenge ID is required")
    private Long challengeId;

    @Schema(description = "Typed content", example = "Hello, world!")
    @NotNull(message = "Typed content is required")
    private String typedContent;

    @Schema(description = "Time taken", example = "10")
    @NotNull(message = "Time taken is required")
    private Integer timeTaken;
}
