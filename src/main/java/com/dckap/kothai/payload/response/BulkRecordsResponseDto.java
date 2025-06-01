package com.dckap.kothai.payload.response;

import com.dckap.kothai.type.BulkItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkRecordsResponseDto {

	private String email;

	private Long employeeId;

	private String employeeName;

	private List<Object> entitlementsDto;

	private BulkItemStatus status;

	private String message;

}
