package com.masato.ka.hls.config;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MineMapperConfigration implements EmbeddedServletContainerCustomizer {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		 MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		    mappings.add("m3u8", "application/x-mpegURL");
		    mappings.add("ts", "video/MP2T");
		    container.setMimeMappings(mappings);

	}

}
