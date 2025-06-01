package com.dckap.kothai.exception;

import com.dckap.kothai.constant.MessageConstant;
import com.dckap.kothai.util.MessageUtil;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Getter
public class ModuleException extends RuntimeException {

	private final transient MessageConstant messageKey;

	private static final AtomicReference<MessageUtil> messageUtil = new AtomicReference<>();

	@Component
	public static class MessageUtilInjector implements ApplicationContextAware {

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) {
			messageUtil.set(applicationContext.getBean(MessageUtil.class));
		}

	}

	public ModuleException(MessageConstant messageKey) {
		super(getMessageUtil().getMessage(messageKey.getMessageKey()));
		this.messageKey = messageKey;
	}

	public ModuleException(MessageConstant messageKey, Object[] args) {
		super(getMessageUtil().getMessage(messageKey.getMessageKey(), args));
		this.messageKey = messageKey;
	}

	private static MessageUtil getMessageUtil() {
		MessageUtil util = messageUtil.get();
		if (util == null) {
			throw new IllegalStateException("MessageUtil not initialized");
		}
		return util;
	}

}
