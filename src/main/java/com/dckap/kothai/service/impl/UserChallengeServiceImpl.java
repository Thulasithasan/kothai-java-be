package com.dckap.kothai.service.impl;

import com.dckap.kothai.model.Challenge;
import com.dckap.kothai.model.User;
import com.dckap.kothai.model.UserChallenge;

import java.util.List;
import java.util.stream.Collectors;

import com.dckap.kothai.type.UserChallengeStatus;
import org.springframework.stereotype.Service;

import com.dckap.kothai.constant.CommonMessageConstant;
import com.dckap.kothai.exception.ModuleException;
import com.dckap.kothai.mapper.CommonMapper;
import com.dckap.kothai.repository.ChallengeRepository;
import com.dckap.kothai.repository.UserChallengeRepository;
import com.dckap.kothai.payload.request.UserChallengeRequestDto;
import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.payload.response.UserChallengeResponseDto;
import com.dckap.kothai.service.UserChallengeService;
import com.dckap.kothai.service.UserService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserChallengeServiceImpl implements UserChallengeService {

    @NonNull
    private final UserChallengeRepository userChallengeRepository;

    @NonNull
    private final UserService userService;

    @NonNull
    private final ChallengeRepository challengeRepository;

    @NonNull
    private final CommonMapper commonMapper;

    @Override
    public ResponseEntityDto createUserChallenge(UserChallengeRequestDto userChallengeRequestDto) {
        log.info("createUserChallenge: execution started");  
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND);
        }

        Challenge challenge = challengeRepository.findByIdAndIsActive(userChallengeRequestDto.getChallengeId(), true)
                .orElseThrow(() -> new ModuleException(CommonMessageConstant.COMMON_ERROR_CHALLENGE_NOT_FOUND));

        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setUser(currentUser);
        userChallenge.setChallenge(challenge);
        userChallenge.setTypedContent(userChallengeRequestDto.getTypedContent());
        userChallenge.setTimeTaken(userChallengeRequestDto.getTimeTaken());

        int accuracy = calculateAccuracy(challenge.getContent(), userChallengeRequestDto.getTypedContent());
        userChallenge.setAccuracy(accuracy);

        int speed = calculateSpeed(userChallengeRequestDto.getTypedContent(), userChallengeRequestDto.getTimeTaken());
        userChallenge.setSpeed(speed);

        userChallenge.setStatus(determineChallengeStatus(challenge, accuracy, speed));

        userChallengeRepository.save(userChallenge);

        UserChallengeResponseDto userChallengeResponseDto = commonMapper.createUserChallengeToUserChallengeResponseDto(userChallenge);  

        log.info("createUserChallenge: execution completed");
        return new ResponseEntityDto(true, userChallengeResponseDto);
    }

    @Override
    public ResponseEntityDto getUserChallenges() {
        log.info("getUserChallenges: execution started");
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND);
        }
        List<UserChallenge> userChallenges = userChallengeRepository.findByUserId(currentUser.getUserId());
        List<UserChallengeResponseDto> userChallengeResponseDtos = userChallenges.stream()
                .map(commonMapper::createUserChallengeToUserChallengeResponseDto)
                .toList();
        log.info("getUserChallenges: execution completed");
        return new ResponseEntityDto(true, userChallengeResponseDtos);
    }

    private UserChallengeStatus determineChallengeStatus(Challenge challenge, int userAccuracy, int userSpeed) {
        boolean meetsAccuracy = userAccuracy >= challenge.getAccuracy();
        boolean meetsSpeed = userSpeed >= challenge.getSpeed();

        if (meetsAccuracy && meetsSpeed) {
            return UserChallengeStatus.COMPLETED;
        } else if (!meetsAccuracy && meetsSpeed) {
            return UserChallengeStatus.LOW_ACCURACY;
        } else if (meetsAccuracy) {
            return UserChallengeStatus.LOW_SPEED;
        } else {
            return UserChallengeStatus.FAILED;
        }
    }

    public static int calculateAccuracy(String actualText, String typedText) {
        if (actualText == null || typedText == null || actualText.isEmpty()) return 0;

        int correctCount = 0;
        int minLength = Math.min(actualText.length(), typedText.length());

        for (int i = 0; i < minLength; i++) {
            if (actualText.charAt(i) == typedText.charAt(i)) {
                correctCount++;
            }
        }

        return (int) Math.round((correctCount * 100.0) / actualText.length());
    }

    public static int calculateSpeed(String typedText, int timeTakenSeconds) {
        if (typedText == null || timeTakenSeconds <= 0) return 0;

        int totalChars = typedText.length();
        double wordsTyped = totalChars / 5.0;
        double minutes = timeTakenSeconds / 60.0;

        return (int) Math.round(wordsTyped / minutes);
    }

}
