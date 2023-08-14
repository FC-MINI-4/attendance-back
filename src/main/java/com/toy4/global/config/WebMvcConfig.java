package com.toy4.global.config;

import com.toy4.global.file.config.FileUploadConfig;
import com.toy4.global.interceptor.LogInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final FileUploadConfig fileUploadConfig;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/images/**")
			.addResourceLocations("file:///" + fileUploadConfig.getFileUploadPath());
		registry
			.addResourceHandler("/images/default/**")
			.addResourceLocations("classpath:/static/images/default/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
				.order(1)
				.addPathPatterns("/**")
				.excludePathPatterns("/images/**");
	}
}
