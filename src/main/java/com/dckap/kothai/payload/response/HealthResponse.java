package com.dckap.kothai.payload.response;

import com.dckap.kothai.type.HealthStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HealthResponse {

	private HealthStatus status;

	private String timestamp;

	private String version;

}
