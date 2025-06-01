package com.dckap.kothai.model;

import com.dckap.kothai.util.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name = "role_permissions")
public class RolePermission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    @ToString.Exclude
    private Permission permission;

    @Convert(converter = StringListConverter.class)
    @Column(name = "sub_permissions", columnDefinition = "jsonb")
    private List<String> subPermissions;

    public RolePermission(RolePermissionId id, Role role, Permission permission, List<String> subPermissions) {
        this.id = id;
        this.role = role;
        this.permission = permission;
        this.subPermissions = subPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePermission that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
