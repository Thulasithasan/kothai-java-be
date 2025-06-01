package com.dckap.kothai.type;

import lombok.Getter;

@Getter
public enum NotificationSort {

	CREATED_DATE("createdDate");

	private final String sortField;

	NotificationSort(String sortField) {
		this.sortField = sortField;
	}

	@Override
	public String toString() {
		return this.sortField;
	}

}
