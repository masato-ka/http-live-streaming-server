package com.masato.ka.hls.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.masato.ka.hls.websocket.handler.AudioWebSocketHandler;

@Configuration
@Component
public class WebsocketConfigure implements WebSocketConfigurer {

	private final AudioWebSocketHandler audioWebSocketHandler;
	
	public WebsocketConfigure(AudioWebSocketHandler audioWebSocketHandler){
		this.audioWebSocketHandler = audioWebSocketHandler;
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(audioWebSocketHandler, "/audio/socket");
	}

}
