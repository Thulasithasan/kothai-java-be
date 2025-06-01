package com.dckap.kothai.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponseDto {

	private String accessToken;

	private UserResponseDto userResponseDto;
}
