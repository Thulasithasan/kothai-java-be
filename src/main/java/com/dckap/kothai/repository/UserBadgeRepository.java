package com.dckap.kothai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.dckap.kothai.model.UserBadge;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    Optional<UserBadge> findTopByUserUserIdOrderByCreatedDateDesc(Long userId);
}
