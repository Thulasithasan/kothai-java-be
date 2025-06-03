package com.dckap.kothai.service.impl;

import com.dckap.kothai.component.AsyncEmailSender;
import com.dckap.kothai.model.RolePermission;
import com.dckap.kothai.model.Role;
import com.dckap.kothai.payload.request.*;
import com.dckap.kothai.payload.response.*;
import com.dckap.kothai.model.User;
import com.dckap.kothai.exception.ModuleException;
import com.dckap.kothai.mapper.CommonMapper;
import com.dckap.kothai.constant.CommonMessageConstant;
import com.dckap.kothai.repository.UserRepository;
import com.dckap.kothai.service.*;
import com.dckap.kothai.util.Validation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final UserDetailsService userDetailsService;

    @NonNull
    private final JwtService jwtService;

    @NonNull
    private final AuthenticationManager authenticationManager;

    @NonNull
    private final PasswordEncoder passwordEncoder;

    @NonNull
    private final CommonMapper commonMapper;

    @NonNull
    private final UserService userService;

    private final AsyncEmailSender asyncEmailSender;

    @Value("${encryptDecryptAlgorithm.secret}")
    private String encryptSecret;

    @Value("${initial.xp}")
    private String initialXp;

    @Override
    public ResponseEntityDto signUp(SignUpRequestDto signUpRequestDto) {
        log.info("signUp: execution started");

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_ALREADY_EXISTS);
        }

        Validation.isValidFirstName(signUpRequestDto.getFirstName());
        Validation.isValidLastName(signUpRequestDto.getLastName());
        Validation.validateEmail(signUpRequestDto.getEmail());

        String generatedPassword = generatePassword();

        User user = commonMapper.createSignUpRequestDtoToUser(signUpRequestDto);
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setIsPasswordChangedForTheFirstTime(false);
        user.setRole(signUpRequestDto.getRoleId() == null
                ? Role.builder().roleId(3L).build()
                : Role.builder().roleId(signUpRequestDto.getRoleId()).build());

        user.setTotalXp(Integer.parseInt(initialXp));

        userRepository.save(user);

        String subject = "Welcome to Kothai! Your Account Details";

        String htmlBody = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2>Welcome, " + user.getFirstName() + " " + user.getLastName() + "!</h2>" +
                "<p>We're excited to have you on board.</p>" +
                "<p>Your account has been created successfully.</p>" +
                "<p><strong>Your temporary password:</strong> <span style='color: #d6336c;'>" + generatedPassword + "</span></p>" +
                "<p>Please make sure to change your password after your first login.</p>" +
                "<br>" +
                "<p>Best regards,<br><strong>Kothai Team</strong></p>" +
                "</body>" +
                "</html>";

        asyncEmailSender.sendMail(user.getEmail(), subject, htmlBody);

        log.info("signUp: execution ended");
        return new ResponseEntityDto("Password has been sent to your email. Please check.", true);
    }


    @Override
    public ResponseEntityDto signIn(SignInRequestDto signInRequestDto) {
        log.info("signIn: execution started");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(), signInRequestDto.getPassword()));

        Optional<User> optionalUser = userRepository.findByEmail(signInRequestDto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_ACCOUNT_DEACTIVATED);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails, user.getUserId());
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        SignInResponseDto signInResponseDto = commonMapper.createUserToSignInResponseDto(user);
        signInResponseDto.setAccessToken(accessToken);
        signInResponseDto.setRefreshToken(refreshToken);
        signInResponseDto.setRoleResponseDto(createRoleResponseDto(user.getRole()));

        log.info("signIn: execution ended");
        return new ResponseEntityDto(true, signInResponseDto);
    }

    @Override
    public ResponseEntityDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        log.info("refreshAccessToken: execution started");

        if (!jwtService.isRefreshToken(refreshTokenRequestDto.getRefreshToken())
                || jwtService.isTokenExpired(refreshTokenRequestDto.getRefreshToken())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_INVALID_REFRESH_TOKEN);
        }

        String userEmail = jwtService.extractUserEmail(refreshTokenRequestDto.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.isTokenValid(refreshTokenRequestDto.getRefreshToken(), userDetails)) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_INVALID_REFRESH_TOKEN);
        }

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        UserResponseDto userToUserResponseDto = commonMapper.createUserToUserResponseDto(user);
        userToUserResponseDto.setRoleResponseDto(createRoleResponseDto(user.getRole()));
        String accessToken = jwtService.generateAccessToken(userDetails, user.getUserId());

        AccessTokenResponseDto accessTokenResponseDto = new AccessTokenResponseDto();
        accessTokenResponseDto.setAccessToken(accessToken);
        accessTokenResponseDto.setUserResponseDto(userToUserResponseDto);

        log.info("refreshAccessToken: execution ended");
        return new ResponseEntityDto(false, accessTokenResponseDto);
    }

    @Override
    public ResponseEntityDto employeeResetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        log.info("employeeResetPassword: execution started");

        User user = userService.getCurrentUser();
        if (user == null) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND);
        }

        if (Boolean.TRUE.equals(user.getIsPasswordChangedForTheFirstTime())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_ALREADY_PASSWORD_RESET);
        }

        String newPassword = resetPasswordRequestDto.getNewPassword();
        createNewPassword(newPassword, user);

        log.info("employeeResetPassword: execution ended");
        return new ResponseEntityDto(false, "User password reset successfully");
    }

    @Override
    public ResponseEntityDto forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        log.info("forgotPassword: execution started");


        log.info("forgotPassword: execution ended");
        return new ResponseEntityDto(false, "The email has been successfully sent to all people admins.");
    }

    @Override
    public ResponseEntityDto changePassword(ChangePasswordRequestDto changePasswordRequestDto, Long userId) {
        log.info("changePassword: execution started");

        User user = userService.getCurrentUser();
        if (!Objects.equals(user.getUserId(), userId)) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_OLD_PASSWORD_INCORRECT);
        }

        String newPassword = changePasswordRequestDto.getNewPassword();
        createNewPassword(newPassword, user);

        log.info("changePassword: execution ended");
        return new ResponseEntityDto(false, "User password changed successfully");
    }

    private void createNewPassword(String newPassword, User user) {
        if (user.getPreviousPasswordsList()
                .stream()
                .anyMatch(prevPassword -> passwordEncoder.matches(newPassword, prevPassword))) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_CANNOT_USE_PREVIOUS_PASSWORDS);
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        if (user.getPassword() != null) {
            user.addPreviousPassword(user.getPassword());
        }

        user.setPassword(encodedNewPassword);
        user.setIsPasswordChangedForTheFirstTime(true);

        userRepository.save(user);
    }

    private String generatePassword() {
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        log.info("tempPassword: {}", tempPassword);
        return tempPassword;
    }

    private RoleResponseDto createRoleResponseDto(Role role) {
        return RoleResponseDto.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getRolePermissions().stream().map(this::createPermissionResponseDto).toList())
                .build();
    }

    private PermissionResponseDto createPermissionResponseDto(RolePermission rolePermission) {
        return PermissionResponseDto.builder()
                .permissionId(rolePermission.getPermission().getPermissionId())
                .name(rolePermission.getPermission().getName())
                .description(rolePermission.getPermission().getDescription())
                .subPermissions(rolePermission.getSubPermissions())
                .build();
    }
}
