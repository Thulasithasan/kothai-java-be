package com.dckap.kothai.service.impl;

import com.dckap.kothai.model.*;
import com.dckap.kothai.repository.UserBadgeRepository;
import org.springframework.stereotype.Service;
import com.dckap.kothai.service.BadgeService;
import com.dckap.kothai.repository.BadgeRepository;
import com.dckap.kothai.payload.request.BadgeRequestDto;
import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.mapper.CommonMapper;
import com.dckap.kothai.exception.ModuleException;
import com.dckap.kothai.constant.CommonMessageConstant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;

    private final CommonMapper commonMapper;

    private final UserBadgeRepository userBadgeRepository;

    @Override
    public ResponseEntityDto createBadge(BadgeRequestDto badgeRequestDto) {
        log.info("createBadge: execution started");

        if (badgeRepository.existsByName(badgeRequestDto.getName())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_BADGE_NAME_ALREADY_EXISTS);
        }

        if (badgeRepository.existsByCriteriaTypeAndCriteriaOpAndCriteriaValue(badgeRequestDto.getCriteriaType(), badgeRequestDto.getCriteriaOp(), badgeRequestDto.getCriteriaValue())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_BADGE_CRITERIA_TYPE_AND_VALUE_ALREADY_EXISTS);
        }
        Badge badge = commonMapper.createBadgeRequestDtoToBadge(badgeRequestDto);
        badgeRepository.save(badge);
        log.info("createBadge: execution ended");
        return new ResponseEntityDto("Badge created successfully", true);
    }

    @Override
    public Badge assignBadges(User user, Challenge challenge, double accuracy, double speed) {
        log.info("Calculating badges for user: {}", user.getUserId());

        List<Badge> allBadges = badgeRepository.findAll();

        for (Badge badge : allBadges) {
            if (meetsCriteria(badge, accuracy, speed)) {
                UserBadge userBadge = new UserBadge();
                userBadge.setUser(user);
                userBadge.setBadge(badge);
                userBadge.setChallenge(challenge);
                userBadgeRepository.save(userBadge);
                log.info("Badge awarded: {} to user: {}", badge.getName(), user.getUserId());
                return badge;
            }
        }
        return null;
    }


    private boolean meetsCriteria(Badge badge, double accuracy, double speed) {
        String type = badge.getCriteriaType(); // e.g., "accuracy", "wpm"
        String op = badge.getCriteriaOp();     // e.g., ">=", "<=", "=="
        double value = badge.getCriteriaValue();

        double userValue = type.equalsIgnoreCase("accuracy") ? accuracy :
                (type.equalsIgnoreCase("speed") ? speed : 0);

        return switch (op) {
            case ">=" -> userValue >= value;
            case "<=" -> userValue <= value;
            case ">" -> userValue > value;
            case "<" -> userValue < value;
            case "==" -> userValue == value;
            default -> false;
        };
    }

}
