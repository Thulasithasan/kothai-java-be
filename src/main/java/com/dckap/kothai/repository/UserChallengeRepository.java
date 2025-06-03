package com.dckap.kothai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dckap.kothai.model.UserChallenge;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId")
    List<UserChallenge> findByUserId(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId AND uc.status='COMPLETED'")
    List<UserChallenge> findByUserIdAndStatusCompleted(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId ORDER BY uc.createdDate DESC")
    Optional<UserChallenge> findTopByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);

    @Query(" SELECT uc FROM UserChallenge uc JOIN uc.challenge c WHERE uc.user.id = :userId AND uc.status='COMPLETED' ORDER BY c.level DESC limit 1")
    Optional<UserChallenge> findTopByUserIdOrderByChallengeLevelDesc(@Param("userId") Long userId);
}
