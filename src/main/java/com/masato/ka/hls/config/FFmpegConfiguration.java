package com.masato.ka.hls.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;

@Configuration
public class FFmpegConfiguration {

	@Value("${ffmpeg.ffmpeg:/usr/local/bin/ffmpeg}")
	private String ffmpegBinaryPath;
	@Value("${ffmpeg.ffprobe:/usr/local/bin/ffprobe}")
	private String ffprobeBinaryPath;
	
    @Bean
    public FFmpeg ffmpeg() throws IOException {
        return new FFmpeg(ffmpegBinaryPath);
    }
    
    @Bean
    public FFprobe ffprobe() throws IOException {
        return new FFprobe(ffprobeBinaryPath);
    }
    
    @Bean
    public FFmpegExecutor ffmpegExecuter(FFmpeg ffmpeg, FFprobe ffprobe){
    	return new FFmpegExecutor(ffmpeg, ffprobe);
    }
}
