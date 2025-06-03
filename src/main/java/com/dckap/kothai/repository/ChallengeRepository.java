package com.dckap.kothai.repository;

import com.dckap.kothai.model.Challenge;
import com.dckap.kothai.type.ChallengeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("SELECT c FROM Challenge c WHERE " +
            "(:type IS NULL OR c.type = :type) AND " +
            "(:isActive IS NULL OR c.isActive = :isActive)")
    Page<Challenge> findByFiltersWithPagination(@Param("type") ChallengeType type,
                                                @Param("isActive") Boolean isActive,
                                                Pageable pageable);

    @Query("SELECT c FROM Challenge c " +
            "WHERE (:type IS NULL OR c.type = :type) " +
            "AND (:isActive IS NULL OR c.isActive = :isActive)")
    List<Challenge> findByFilters(
            @Param("type") ChallengeType type,
            @Param("isActive") Boolean isActive
    );


    Page<Challenge> findByCreatedBy(String userId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Challenge c WHERE LOWER(c.title) = LOWER(:title)")
    boolean isExitTitle(String title);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Challenge c WHERE c.type = :type AND c.level = :level")
    boolean isExitTypeAndLevel(ChallengeType type, Integer level);

    @Query("SELECT c FROM Challenge c WHERE c.id = :id AND c.isActive = :isActive")
    Optional<Challenge> findByIdAndIsActive(@Param("id") Long id, @Param("isActive") boolean isActive);

    @Query("SELECT c FROM Challenge c WHERE c.type = :type ORDER BY c.level ASC")
    List<Challenge> findByType(@Param("type") ChallengeType type);

    @Query("SELECT COUNT(c) FROM Challenge c WHERE c.type = :type AND c.isActive = :isActive")
    Long countByTypeAndIsActive(@Param("type") ChallengeType type, @Param("isActive") boolean isActive);

}
