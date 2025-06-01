package com.dckap.kothai.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    @SequenceGenerator(name = "ROLE_SEQ", sequenceName = "SEQ_OT_ROLE", allocationSize = 1)
    private Long roleId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<RolePermission> rolePermissions = new HashSet<>();

    public void addPermission(Permission permission, List<String> subPermissions) {
        RolePermissionId rolePermissionId = new RolePermissionId(this.roleId, permission.getPermissionId());
        RolePermission rolePermission = new RolePermission(rolePermissionId, this, permission, subPermissions);
        rolePermissions.add(rolePermission);
    }
}


