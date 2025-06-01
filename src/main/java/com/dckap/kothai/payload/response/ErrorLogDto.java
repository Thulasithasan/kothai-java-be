package com.dckap.kothai.payload.response;

import com.dckap.kothai.type.BulkItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLogDto {

	private String email;

	private Long employeeId;

	private String employeeName;

	private BulkItemStatus status;

	private String message;

}
