package com.dckap.kothai.payload.response;

import java.util.HashMap;

public class Acknowledgement extends HashMap<String, Object> {

	public Acknowledgement(String message) {
		put("message", message);
	}

}
