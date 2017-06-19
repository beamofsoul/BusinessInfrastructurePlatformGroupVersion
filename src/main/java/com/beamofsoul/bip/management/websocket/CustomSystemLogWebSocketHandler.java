package com.beamofsoul.bip.management.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.beamofsoul.bip.management.util.CollectionUtils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName CustomSystemLogWebSocketHandler
 * @Description 系统消息websocket行为控制器
 * @author MingshuJian
 * @Date 2017年3月21日 下午3:19:34
 * @version 1.0.0
 */
@Slf4j
@Async
@Component
public class CustomSystemLogWebSocketHandler implements WebSocketHandler {

	/**
	 * LongAdder是比AtomicInteger性能更好的，源于JDK8的实现方式，可以减少乐观锁的重试次数
	 */
	private static LongAdder onlineCount = new LongAdder();
	private static CopyOnWriteArraySet<WebSocketSession> webSocketSet = new CopyOnWriteArraySet<WebSocketSession>();
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		webSocketSet.remove(session);
		onlineCount.decrement();
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		webSocketSet.add(session);
		onlineCount.increment();
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		//暂时不需要接受客户端传递消息的功能...
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		exception.printStackTrace();
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	/**
	 * @Title: sendMessageToAll  
	 * @Description: 发送消息至所有激活用户
	 * @param message - 需要被发送的消息  
	 */
	public static void sendMessageToAll(WebSocketMessage<?> message) {
		String content = message.getPayload().toString();
		for (WebSocketSession session : webSocketSet) {
			if (session.isOpen()) {
//				session.sendMessage(new TextMessage(content));
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if (session.isOpen()) session.sendMessage(new TextMessage(content));
						} catch (IOException e) {
							log.error("failed to send message {} to user {}", content, session.getPrincipal().getName());
						}
					}
				}).start();
			}
		}
	}
	
	/**
	 * @Title: sendMessageToUsers 
	 * @Description: 将消息发送给某些激活用户
	 * @param message - 需要被发送的消息
	 * @param usernames - 需要接受消息的用户们
	 */
	public static void sendMessageToUsers(@NonNull WebSocketMessage<?> message, @NonNull List<String> usernames) {
		List<WebSocketSession> list = webSocketSet.stream().filter(e -> e.isOpen() && usernames.contains(e.getPrincipal().getName())).collect(Collectors.toList());
		String content = message.getPayload().toString();
		if (CollectionUtils.isNotBlank(list)) {
			list.stream().forEach(
				e -> {
					try {
						e.sendMessage(new TextMessage(content));
					} catch (Exception exception) {
						log.error("failed to send message {} to user {}", content, e.getPrincipal().getName(), exception);
					}
				}
			);
		} else {
			log.error("failed to send message {} to users {}, cause cannot find alive users", content, usernames.toArray().toString());
		}
	}
	
	/**
	 * @Title: sendMessageToUser  
	 * @Description: 将消息发送给某个激活用户
	 * @param message - 需要被发送的消息
	 * @param username - 需要接受消息的用户  
	 */
	public static void sendMessageToUser(@NonNull WebSocketMessage<?> message, @NonNull String username) {
		List<WebSocketSession> list = webSocketSet.stream().filter(e -> e.isOpen() && e.getPrincipal().getName().equals(username)).collect(Collectors.toList());
		String content = message.getPayload().toString();
		if (CollectionUtils.isNotBlank(list)) {
			WebSocketSession session = list.get(0);
			try {
				session.sendMessage(new TextMessage(content));
			} catch (IOException e) {
				log.error("failed to send message {} to user {}", content, username, e);
			}
		} else {
			log.error("failed to send message {} to user {}, cause cannot find alive user", content, username);
		}
	}
	
	public static Integer getOnlineCount() {
		return onlineCount.intValue();
	}

}
