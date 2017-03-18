package com.masato.ka.hls.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class AudioFile {
	
	// 16bit * 16kHz * 30sec
	private static final int maxBufferSize = 2*16000*30;
	
	@Setter @Getter
	private String fileName;	
	@Setter @Getter
	private byte[] rawAudioData = new byte[maxBufferSize];
	@Getter
	private int bufferSize=0;
	@Getter
	private int remainBufferSize=maxBufferSize;
	/**
	 * バッファ領域にデータをコピーする。
	 * 
	 * @param rawAudioData コピーするバッファ
	 * @return コピーされなかったバッファのサイズ
	 */
	public int copyAudioData(byte[] data){

		int writeSize = remainBufferSize > data.length ? 
					data.length : remainBufferSize;
		System.arraycopy(data, 0, rawAudioData, bufferSize, writeSize);
		bufferSize += writeSize;
		remainBufferSize = maxBufferSize - bufferSize;
		return data.length - writeSize;
	}
	
	public void resetBuffer(){
		bufferSize = 0;
		remainBufferSize = maxBufferSize;
	}
	
	
}
