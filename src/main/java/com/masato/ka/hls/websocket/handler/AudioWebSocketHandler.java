package com.masato.ka.hls.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;


@Component
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

	/**
	 * 接続後処理
	 * 接続後、オーディオ情報格納領域を確保する。
	 * @param session Websocketのセッション情報
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		
	}
	
	/**
	 * 切断後処理
	 * 切断されたセッションのオーディオ情報格納領域をリリースする。
	 * @param session WebSocketのセッション情報
	 * @param status 切断ステータス情報
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
		throws Exception{
		
	}
	
	/**
	 * sessionに対応したオーディオデータ格納領域にデータを格納する。一定量データが溜まったら
	 * 非同期でHLSの配信ファイル(.ts .m3u8)作成処理を呼び出す。
	 * @param session WebSocketのセッション情報
	 * @param message 受信したバイナリデータ
	 */
	@Override
	public void handleBinaryMessage(WebSocketSession session, BinaryMessage message){
		
	}
	
}
