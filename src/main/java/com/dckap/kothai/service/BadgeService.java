package com.dckap.kothai.service;

import com.dckap.kothai.model.Badge;
import com.dckap.kothai.model.Challenge;
import com.dckap.kothai.model.User;
import com.dckap.kothai.payload.request.BadgeRequestDto;
import com.dckap.kothai.payload.response.ResponseEntityDto;

public interface BadgeService {
    ResponseEntityDto createBadge(BadgeRequestDto badgeRequestDto);

    Badge assignBadges(User user, Challenge challenge, double accuracy, double speed);
}
