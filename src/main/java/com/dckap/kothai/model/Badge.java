package com.dckap.kothai.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "badges")
public class Badge extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id", nullable = false, updatable = false)
    private Long badgeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "criteria_type", nullable = false)
    private String criteriaType;

    @Column(name = "criteria_op", nullable = false)
    private String criteriaOp;

    @Column(name = "criteria_value", nullable = false)
    private double criteriaValue;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
