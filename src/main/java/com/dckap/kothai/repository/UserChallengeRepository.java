package com.dckap.kothai.repository;

import java.util.List;
import java.util.Optional;

import com.dckap.kothai.type.UserChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dckap.kothai.model.UserChallenge;
import com.dckap.kothai.type.ChallengeType;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId")
    List<UserChallenge> findByUserId(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId AND uc.status='COMPLETED'")
    List<UserChallenge> findByUserIdAndStatusCompleted(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId ORDER BY uc.createdDate DESC")
    Optional<UserChallenge> findTopByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);

    @Query(" SELECT uc FROM UserChallenge uc JOIN uc.challenge c WHERE uc.user.id = :userId " +
            "AND uc.status='COMPLETED' AND c.type = :challengeType ORDER BY c.level DESC limit 1")
    Optional<UserChallenge> findTopByUserIdAndChallengeTypeOrderByChallengeLevelDesc(@Param("userId") Long userId,
                                                                                     @Param("challengeType") ChallengeType challengeType);

    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND (:status IS NULL OR uc.status = :status) " +
            "AND uc.challenge.type = :challengeType " +
            "ORDER BY uc.challenge.level DESC limit 1")
    Optional<UserChallenge> findTopChallenge(
            @Param("userId") Long userId,
            @Param("status") UserChallengeStatus status,
            @Param("challengeType") ChallengeType challengeType);

}
