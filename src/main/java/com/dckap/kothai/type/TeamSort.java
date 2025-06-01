package com.dckap.kothai.type;

import lombok.Getter;

@Getter
public enum TeamSort {

	TEAM_NAME("teamName");

	private final String sortField;

	TeamSort(String sortField) {
		this.sortField = sortField;
	}

	@Override
	public String toString() {
		return this.sortField;
	}

}
