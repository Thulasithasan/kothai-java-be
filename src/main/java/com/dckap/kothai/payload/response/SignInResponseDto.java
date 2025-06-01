package com.dckap.kothai.payload.response;

import com.dckap.kothai.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponseDto {

	private String userId;

	private String firstName;

	private String lastName;

	private String email;

	private RoleResponseDto roleResponseDto;

	private String accessToken;

	private String refreshToken;

	private Boolean isPasswordChangedForTheFirstTime;

}
