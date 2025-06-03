package com.dckap.kothai.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dckap.kothai.model.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    boolean existsByName(String name);

    boolean existsByCriteriaTypeAndCriteriaOpAndCriteriaValue(String criteriaType, String criteriaOp, double criteriaValue);

    boolean existsByCriteriaTypeAndCriteriaValue(String criteriaType, double criteriaValue);
}
