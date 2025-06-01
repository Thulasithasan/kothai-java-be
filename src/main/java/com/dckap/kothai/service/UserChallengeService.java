package com.dckap.kothai.service;

import com.dckap.kothai.payload.request.UserChallengeRequestDto;
import com.dckap.kothai.payload.response.ResponseEntityDto;

public interface UserChallengeService {

    ResponseEntityDto createUserChallenge(UserChallengeRequestDto userChallengeRequestDto);

    ResponseEntityDto getUserChallenges();
}
