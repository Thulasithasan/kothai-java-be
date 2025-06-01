package com.dckap.kothai.util.event;

import com.dckap.kothai.model.User;

public class UserCreatedEvent extends UserEvent {

	public UserCreatedEvent(Object source, User user) {
		super(source, user);
	}

}
