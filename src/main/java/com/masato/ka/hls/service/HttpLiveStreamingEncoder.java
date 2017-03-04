package com.masato.ka.hls.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.masato.ka.hls.model.AudioFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HttpLiveStreamingEncoder {
	private final int BUFSIZE = 1 <<4;
	private int writePointer = 0;
	private int readPointer = 0;
	private AudioFile[] tsFileRing = new AudioFile[BUFSIZE];
	
	public void writeStreaming(byte[] audioRawData){
		if(tsFileRing[writePointer % BUFSIZE] == null){
			tsFileRing[writePointer % BUFSIZE] = new AudioFile();
		}
		int remainSize = tsFileRing[writePointer % BUFSIZE].copyAudioData(audioRawData);
		if(remainSize > 0){
			byte[] remainBuf = new byte[remainSize];
			System.arraycopy(audioRawData, audioRawData.length-remainSize, remainBuf, 0, remainSize);
			tsFileRing[writePointer+1 % BUFSIZE].copyAudioData(remainBuf);
		}
		
		if(tsFileRing[writePointer % BUFSIZE].getRemainBufferSize() <= 0){
			writePointer++;
		}
		
	}
	
	public void flushWaveFile(){
		
		int p = readPointer % BUFSIZE;
		if(tsFileRing[p].getRemainBufferSize() > 0){
			return;
		}
		String fileName = RandomStringUtils.randomAlphabetic(20);
		byte[] audioData = tsFileRing[p].getRawAudioData();
		AudioFormat audioFormat = new AudioFormat(16000,16,1,true,false);
		InputStream inputStream = new ByteArrayInputStream(audioData);
		AudioInputStream audioInputStream = 
				new AudioInputStream(inputStream, audioFormat, audioData.length);
		AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;
		File sound = new File(fileName+".wav");
		try {
			AudioSystem.write(audioInputStream, type, sound);
		} catch (IOException e) {
			log.error("failed write sound file.");
			e.printStackTrace();
		}
		log.info("flush file");
		readPointer += 1;
		
	}

}
