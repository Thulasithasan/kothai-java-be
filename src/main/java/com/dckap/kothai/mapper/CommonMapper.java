package com.dckap.kothai.mapper;

import com.dckap.kothai.model.Badge;
import com.dckap.kothai.model.Challenge;
import com.dckap.kothai.model.User;
import com.dckap.kothai.model.UserChallenge;
import com.dckap.kothai.payload.request.BadgeRequestDto;
import com.dckap.kothai.payload.request.ChallengeRequestDto;
import com.dckap.kothai.payload.request.SignUpRequestDto;
import com.dckap.kothai.payload.response.ChallengeResponseDto;
import com.dckap.kothai.payload.response.SignInResponseDto;
import com.dckap.kothai.payload.response.UserChallengeResponseDto;
import com.dckap.kothai.payload.response.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    User createSignUpRequestDtoToUser(SignUpRequestDto signUpRequestDto);

    SignInResponseDto createUserToSignInResponseDto(User user);

    UserResponseDto createUserToUserResponseDto(User user);

    Challenge createChallengeRequestDtoToChallenge(ChallengeRequestDto challengeRequestDto);

    ChallengeResponseDto createChallengeToChallengeResponseDto(Challenge challenge);

    UserChallengeResponseDto createUserChallengeToUserChallengeResponseDto(UserChallenge userChallenge);

    Badge createBadgeRequestDtoToBadge(BadgeRequestDto badgeRequestDto);
}
