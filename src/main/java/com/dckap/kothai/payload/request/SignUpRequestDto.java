package com.dckap.kothai.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @Schema(description = "User's email address", example = "user@example.com")
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@Schema(description = "User's first name", example = "John")
	@NotBlank(message = "First name is required")
	private String firstName;

	@Schema(description = "User's last name", example = "Doe")
	@NotBlank(message = "Last name is required")
	private String lastName;

	@Schema(description = "User's role ID", example = "1")
	@NotNull(message = "Role ID is required")
	private Long roleId;
}
