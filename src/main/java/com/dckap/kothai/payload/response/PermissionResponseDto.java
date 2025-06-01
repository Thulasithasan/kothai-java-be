package com.dckap.kothai.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PermissionResponseDto {
    private Long permissionId;
    private String name;
    private String description;
    private List<String> subPermissions;
}
