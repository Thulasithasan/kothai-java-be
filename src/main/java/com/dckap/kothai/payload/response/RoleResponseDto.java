package com.dckap.kothai.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleResponseDto {
    private Long roleId;
    private String name;
    private String description;
    private List<PermissionResponseDto> permissions;
}
