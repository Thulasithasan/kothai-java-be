package com.dckap.kothai.service;

import com.dckap.kothai.payload.request.*;
import com.dckap.kothai.payload.response.ResponseEntityDto;

public interface AuthService {

	ResponseEntityDto signIn(SignInRequestDto signInRequestDto);

	ResponseEntityDto signUp(SignUpRequestDto signUpRequestDto);

	ResponseEntityDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto);

	ResponseEntityDto employeeResetPassword(ResetPasswordRequestDto resetPasswordRequestDto);

	ResponseEntityDto forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto);

	ResponseEntityDto changePassword(ChangePasswordRequestDto changePasswordRequestDto, Long userId);
}
