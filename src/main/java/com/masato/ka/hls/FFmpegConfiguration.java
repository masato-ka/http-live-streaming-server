package com.masato.ka.hls;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;

@Configuration
public class FFmpegConfiguration {

    @Bean
    public FFmpeg ffmpeg() throws IOException {
        return new FFmpeg("/usr/local/bin/ffmpeg");
    }
    
    @Bean
    public FFprobe ffprobe() throws IOException {
        return new FFprobe("/usr/local/bin/ffprobe");
    }
    
    @Bean
    public FFmpegExecutor ffmpegExecuter(FFmpeg ffmpeg, FFprobe ffprobe){
    	return new FFmpegExecutor(ffmpeg, ffprobe);
    }
}
