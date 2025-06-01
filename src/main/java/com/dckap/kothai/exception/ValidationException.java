package com.dckap.kothai.exception;

import com.dckap.kothai.constant.MessageConstant;
import com.dckap.kothai.util.MessageUtil;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class ValidationException extends RuntimeException {

	private final transient MessageConstant messageKey;

	private final transient List<String> validationErrors;

	private static final AtomicReference<MessageUtil> messageUtil = new AtomicReference<>();

	@Component
	public static class MessageUtilInjector implements ApplicationContextAware {

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) {
			messageUtil.set(applicationContext.getBean(MessageUtil.class));
		}

	}

	public ValidationException(MessageConstant messageKey) {
		super(getMessageUtil().getMessage(messageKey.getMessageKey()));
		this.messageKey = messageKey;
		this.validationErrors = null;
	}

	public ValidationException(MessageConstant messageKey, List<String> validationErrors) {
		super(getMessageUtil().getMessage(messageKey.getMessageKey(),
				new Object[] { String.join(", ", validationErrors) }));
		this.messageKey = messageKey;
		this.validationErrors = validationErrors;
	}

	private static MessageUtil getMessageUtil() {
		MessageUtil util = messageUtil.get();
		if (util == null) {
			throw new IllegalStateException("MessageUtil not initialized");
		}
		return util;
	}

}
