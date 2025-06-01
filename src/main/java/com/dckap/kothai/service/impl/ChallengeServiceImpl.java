package com.dckap.kothai.service.impl;

import com.dckap.kothai.constant.CommonMessageConstant;
import com.dckap.kothai.exception.ModuleException;
import com.dckap.kothai.mapper.CommonMapper;
import com.dckap.kothai.model.Challenge;
import com.dckap.kothai.model.User;
import com.dckap.kothai.model.UserChallenge;
import com.dckap.kothai.payload.request.ChallengeRequestDto;
import com.dckap.kothai.payload.request.ChallengeFilterDto;
import com.dckap.kothai.payload.response.ChallengeResponseDto;
import com.dckap.kothai.payload.response.PageDto;
import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.repository.ChallengeRepository;
import com.dckap.kothai.repository.UserChallengeRepository;
import com.dckap.kothai.service.ChallengeService;
import com.dckap.kothai.service.UserService;
import com.dckap.kothai.type.ChallengeType;
import com.dckap.kothai.util.transformer.PageTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeServiceImpl implements ChallengeService {

    @NonNull
    private final ChallengeRepository challengeRepository;

    @NonNull
    private final CommonMapper commonMapper;

    @NonNull
    private final UserService userService;

    @NonNull
    private final UserChallengeRepository userChallengeRepository;

    private final PageTransformer pageTransformer;

    @Override
    public ResponseEntityDto createChallenge(ChallengeRequestDto challengeRequestDto) {
        log.info("createChallenge: execution started");

        if (challengeRepository.isExitTypeAndLevel(challengeRequestDto.getType(), challengeRequestDto.getLevel())) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_CHALLENGE_TYPE_AND_LEVEL_ALREADY_EXISTS);
        }

        Challenge challenge = commonMapper.createChallengeRequestDtoToChallenge(challengeRequestDto);
        challengeRepository.save(challenge);

        log.info("createChallenge: execution ended");
        return new ResponseEntityDto("challenge created successfully", true);
    }

    @Override
    public ResponseEntityDto fetchChallengeById(Long challengeId) {
        log.info("fetchChallengeById: execution started");
        
        Optional<Challenge> challenge = challengeRepository.findById(challengeId);
        
        if (challenge.isEmpty()) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_CHALLENGE_NOT_FOUND);
        }
        
        ChallengeResponseDto challengeResponseDto = commonMapper.createChallengeToChallengeResponseDto(challenge.get());

        log.info("fetchChallengeById: execution ended");
        return new ResponseEntityDto(true, challengeResponseDto);
    }

    @Override
    public ResponseEntityDto getChallengesByFilter(ChallengeFilterDto challengeFilterDto) {

        if (Boolean.TRUE.equals(challengeFilterDto.getIsPaginationRequired())) {
            Pageable pageable = buildPageable(challengeFilterDto);

            Page<Challenge> challengePage = challengeRepository.findByFiltersWithPagination(
                    challengeFilterDto.getType(),
                    challengeFilterDto.getIsActive(),
                    pageable
            );

            PageDto pageDto = pageTransformer.transform(challengePage);

            pageDto.setItems(challengePage.getContent().stream()
                    .map(commonMapper::createChallengeToChallengeResponseDto).toList());

            return new ResponseEntityDto(true, pageDto);
        }

        List<Challenge> challenges = challengeRepository.findByFilters(challengeFilterDto.getType(),
                challengeFilterDto.getIsActive());

        return new ResponseEntityDto(true, challenges);
    }

    @Override
    public ResponseEntityDto fetchMyChallenges(ChallengeType challengeType) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND);
        }

        Optional<UserChallenge> ouc = userChallengeRepository.findTopByUserIdOrderByChallengeLevelDesc(currentUser.getUserId());

        int unlockedLevel;

        if (ouc.isPresent()) {
            UserChallenge userChallenge = ouc.get();
            unlockedLevel = userChallenge.getChallenge().getLevel() + 1;
        } else {
            unlockedLevel = 1;
        }

        List<ChallengeResponseDto> challengeResponseDtos = challengeRepository.findByType(challengeType).stream()
                .map(challenge -> {
                    ChallengeResponseDto dto = commonMapper.createChallengeToChallengeResponseDto(challenge);
                    dto.setIsBlocked(challenge.getLevel() > unlockedLevel);
                    return dto;
                })
                .toList();

        return new ResponseEntityDto(true, challengeResponseDtos);
    }



    private Pageable buildPageable(ChallengeFilterDto challengeFilterDto) {
        return PageRequest.of(
                Math.max(challengeFilterDto.getPageNumber() - 1, 0),    // Page number is 1-based, but PageRequest is 0-based
                challengeFilterDto.getPageSize(),
                Sort.by(Sort.Direction.ASC, "level")
        );
    }
}
