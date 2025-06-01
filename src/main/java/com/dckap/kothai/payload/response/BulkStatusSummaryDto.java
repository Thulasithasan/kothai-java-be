package com.dckap.kothai.payload.response;

import java.util.concurrent.atomic.AtomicInteger;

public class BulkStatusSummaryDto {

	private AtomicInteger successCount = new AtomicInteger(0);

	private AtomicInteger failedCount = new AtomicInteger(0);

	public void incrementSuccessCount() {
		this.successCount.incrementAndGet();
	}

	public void incrementFailedCount() {
		this.failedCount.incrementAndGet();
	}

	public int getSuccessCount() {
		return successCount.get();
	}

	public int getFailedCount() {
		return failedCount.get();
	}

}
