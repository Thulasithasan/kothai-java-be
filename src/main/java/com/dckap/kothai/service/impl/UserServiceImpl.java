package com.dckap.kothai.service.impl;

import com.dckap.kothai.constant.CommonMessageConstant;
import com.dckap.kothai.exception.ModuleException;
import com.dckap.kothai.mapper.CommonMapper;
import com.dckap.kothai.model.User;
import com.dckap.kothai.model.UserBadge;
import com.dckap.kothai.model.UserChallenge;
import com.dckap.kothai.repository.ChallengeRepository;
import com.dckap.kothai.repository.UserBadgeRepository;
import com.dckap.kothai.repository.UserChallengeRepository;
import com.dckap.kothai.repository.UserRepository;
import com.dckap.kothai.type.ChallengeType;
import com.dckap.kothai.service.UserService;

import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.payload.response.UserResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CommonMapper commonMapper;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND));
    }

    @Override
    public ResponseEntityDto getCurrentUserDetails() {
        log.info("getCurrentUserDetails: execution started");
        User currentUser = getCurrentUser();
        UserResponseDto userResponseDto = commonMapper.createUserToUserResponseDto(currentUser);

        userResponseDto.setTotalChallenges(challengeRepository.countByTypeAndIsActive(ChallengeType.LEVEL, true).intValue());

        Optional<UserChallenge> optionalUserChallenge = userChallengeRepository.
                findTopByUserIdOrderByChallengeLevelDesc(currentUser.getUserId());

        optionalUserChallenge.ifPresent(userChallenge ->
                userResponseDto.setCurrentLevel(userChallenge.getChallenge().getLevel()));

        Optional<UserBadge> optionalUserBadge = userBadgeRepository.
                findTopByUserUserIdOrderByCreatedDateDesc(currentUser.getUserId());

        if (optionalUserBadge.isPresent()) {
            userResponseDto.setCurrentBadgeName(optionalUserBadge.get().getBadge().getName());
            userResponseDto.setCurrentBadgeDescription(optionalUserBadge.get().getBadge().getDescription());
        }

        List<UserChallenge> userChallengeList = userChallengeRepository.findByUserId(currentUser.getUserId());
        List<UserChallenge> userChallengeCompletedList = userChallengeRepository.findByUserIdAndStatusCompleted(currentUser.getUserId());

        int totalChallenges = 0;
        int totalTimespent = 0;
        double totalSpeed = 0;
        double totalAccuracy = 0;
        double bestSpeed = 0;
        double bestAccuracy = 0;
        for (UserChallenge userChallenge : userChallengeCompletedList) {
            totalChallenges++;
            totalSpeed += userChallenge.getSpeed();
            totalAccuracy += userChallenge.getAccuracy();
            bestSpeed = Math.max(bestSpeed, userChallenge.getSpeed());
            bestAccuracy = Math.max(bestAccuracy, userChallenge.getAccuracy());
        }

        for (UserChallenge userChallenge : userChallengeList) {
            totalTimespent += userChallenge.getTimeTaken();
        }

        userResponseDto.setTimespent(totalTimespent);
        userResponseDto.setAvarageSpeed(totalSpeed / totalChallenges);
        userResponseDto.setAvarageAccuracy(totalAccuracy / totalChallenges);
        userResponseDto.setBestSpeed(bestSpeed);
        userResponseDto.setBestAccuracy(bestAccuracy);

        log.info("getCurrentUserDetails: execution ended");
        return new ResponseEntityDto(true, userResponseDto);
    }
}
