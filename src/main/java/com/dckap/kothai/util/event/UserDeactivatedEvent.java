package com.dckap.kothai.util.event;

import com.dckap.kothai.model.User;

public class UserDeactivatedEvent extends UserEvent {

	public UserDeactivatedEvent(Object source, User user) {
		super(source, user);
	}

}
