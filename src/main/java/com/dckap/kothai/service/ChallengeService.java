package com.dckap.kothai.service;

import com.dckap.kothai.payload.request.ChallengeFilterDto;
import com.dckap.kothai.payload.request.ChallengeRequestDto;
import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.type.ChallengeType;

public interface ChallengeService {

    ResponseEntityDto createChallenge(ChallengeRequestDto challengeRequestDto);

    ResponseEntityDto fetchChallengeById(Long challengeId);

    ResponseEntityDto getChallengesByFilter(ChallengeFilterDto challengeFilterDto);

    ResponseEntityDto fetchMyChallenges(ChallengeType challengeType);
}
