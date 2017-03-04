package com.masato.ka.hls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class HttpLiveStreamingEncoderApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpLiveStreamingEncoderApplication.class, args);
	}
}
