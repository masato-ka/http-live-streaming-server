package com.masato.ka.hls.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.masato.ka.hls.model.AudioFile;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@Slf4j
@Service
public class HttpLiveStreamingEncoder {
	
	private final FFmpegExecutor executor;
	
    @Value("{hls.audio.path:static/audio/}")
	private String audioPath;
	private Integer segmentNumber = 0;
	
	public HttpLiveStreamingEncoder(FFmpegExecutor ffmpegExecutor){
		executor = ffmpegExecutor;
	}
	
	
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
		File sound = new File(audioPath+fileName+".wav");
		try {
			AudioSystem.write(audioInputStream, type, sound);
		} catch (IOException e) {
			log.error("failed write sound file.");
			e.printStackTrace();
		}
		readPointer += 1;
		encodeToAAC(audioPath+fileName+".wav",audioPath+fileName+".aac");
		publishTS(audioPath+fileName + ".aac");
		rebuildManifest();
		log.info("flush file");
	}
	
	private void encodeToAAC(String inputFileName, String outputFileName){
		FFmpegBuilder builder = new FFmpegBuilder()
				.setInput(inputFileName)
				.overrideOutputFiles(true)
				.addOutput(outputFileName)
			    .setAudioCodec("aac")        
			    .setAudioBitRate(128000)     
			    .done();
		executor.createJob(builder).run();
	}

	private void publishTS(String inputFileName){

		FFmpegBuilder builder = new FFmpegBuilder()
				.setInput(inputFileName)
				.addOutput(audioPath+"output.m3u8")
				.setAudioCodec("copy")
				.addExtraArgs("-hls_list_size", "0")
				.addExtraArgs("-hls_time","9")
				.addExtraArgs("-start_number", "0")
				.addExtraArgs("-hls_playlist_type", "event")
				.addExtraArgs("-hls_flags", "append_list")
				.done();
		executor.createJob(builder).run();	    
		segmentNumber += 4;
	}
	
	private void rebuildManifest(){
		List<String> lines = new ArrayList<>();
		try(BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(audioPath+"output.m3u8"),"UTF-8"))){
			String line=null;
			while((line = reader.readLine()) != null){
				lines.add(line);
			}
		}catch(IOException e){
			log.error("failed read file.");
		}
		try(BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(audioPath+"sample.m3u8"),"UTF-8"))){
			
			for(int i=0; i < lines.size()-1;++i){
				writer.write(lines.get(i));
				writer.newLine();
			}
				
		}catch(IOException e){
			log.error("failed write file.");
		}
		
	}
	
}
