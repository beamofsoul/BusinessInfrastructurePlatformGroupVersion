package com.beamofsoul.bip.management.websocket;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @ClassName CustomWebSocketConfiguration
 * @Description 自定义websocket配置类
 * @author MingshuJian
 * @Date 2017年3月21日 下午3:17:47
 * @version 1.0.0
 */
@Configuration
@EnableWebSocket
public class CustomWebSocketConfiguration implements WebSocketConfigurer {

	@Resource
	private WebSocketHandler customSystemLogWebSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//系统日志WebSocket
		registry.addHandler(customSystemLogWebSocketHandler, "/systemLog").withSockJS();
	}

}
