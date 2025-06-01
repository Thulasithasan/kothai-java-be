package com.dckap.kothai.payload.response;

import com.dckap.kothai.payload.response.RoleResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

	private Long userId;

	private String email;

	private String firstName;

	private String lastName;

	private RoleResponseDto roleResponseDto;
}
