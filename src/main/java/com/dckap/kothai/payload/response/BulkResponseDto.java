package com.dckap.kothai.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkResponseDto {

	private List<ErrorLogDto> bulkRecordErrorLogs;

	private BulkStatusSummaryDto bulkStatusSummary;

}
