package com.dckap.kothai.component;

public interface AsyncEmailSender {

	void sendMail(String to, String subject, String htmlBody);

}
