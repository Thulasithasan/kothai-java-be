package com.dckap.kothai.config;

import com.dckap.kothai.component.WebSocketAuthInterceptor;
import com.dckap.kothai.component.WebSocketHandler;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final WebSocketHandler webSocketHandler;

	private final WebSocketAuthInterceptor authInterceptor;

	@Override
	public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/ws/notification")
			.addInterceptors(authInterceptor)
			.setAllowedOrigins("*");
	}

}
