package com.dckap.kothai.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class ResponseEntityDto {

	private static final String SUCCESSFUL = "successful";

	private static final String UNSUCCESSFUL = "unsuccessful";

	private String status;

	private List<Object> results;

	public ResponseEntityDto() {
		status = SUCCESSFUL;
		results = new ArrayList<>();
	}

	public ResponseEntityDto(String message, boolean successful) {
		this();
		this.status = successful ? SUCCESSFUL : UNSUCCESSFUL;
		putToResults(new Acknowledgement(message));
	}

	public ResponseEntityDto(boolean successful, Object data) {
		this();
		status = successful ? SUCCESSFUL : UNSUCCESSFUL;
		putToResults(data);
	}

	public ResponseEntityDto(boolean successful, List<Object> data) {
		this();
		status = successful ? SUCCESSFUL : UNSUCCESSFUL;
		putToResults(data);
	}

	protected void putToResults(Object data) {
		if (data != null) {

			if (data instanceof Collection<?>) {
				results.addAll((Collection<?>) data);
			}
			else {
				results.add(data);
			}
		}
	}

}
