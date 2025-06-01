package com.dckap.kothai.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReInvitationSkippedCountDto {

	private int skippedCount;

	public void incrementSkippedCount() {
		this.skippedCount++;
	}

}
