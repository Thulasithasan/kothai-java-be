package com.dckap.kothai.component;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileActivator {

	private final Environment environment;

	public boolean isEpProfile() {
		String[] profiles = environment.getActiveProfiles();
		for (String profile : profiles) {
			if (profile.startsWith("ep-")) {
				return true;
			}
		}

		return false;
	}

}
