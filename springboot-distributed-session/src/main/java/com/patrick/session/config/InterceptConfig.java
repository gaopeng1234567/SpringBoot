package com.patrick.session.config;

import com.patrick.session.config.properties.NoAuthUrlProperties;
import com.patrick.session.intercepter.AuthInterceptorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

/**
 * ｜                 ,;,,;;
 * ｜               ,;;'()     风
 * ｜     __      ,;;' ' \     流
 * ｜  /'  '\'~~'~' \ /'\.)    堪
 * ｜ ,;(      )    /  |.      比
 * ｜,;' \    /-.,,(   ) \     丶
 * ｜    ) /       ) / )|      城
 * ｜    ||        ||  \)      管
 * ｜   (_\       (_\          希
 * @author: 城管丶希
 * @date: Created in 2021/5/11 3:31 下午
 * @desc:
 * @motto: Keep It Simple and Stupid, KISS
 */
@EnableConfigurationProperties(NoAuthUrlProperties.class)
@Configuration
public class InterceptConfig implements WebMvcConfigurer {
	@Autowired
	private NoAuthUrlProperties noAuthUrlProperties;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptorHandler())
				.addPathPatterns("/**")
				.excludePathPatterns(new ArrayList<>(noAuthUrlProperties.getShouldSkipUrls()));
	}

	@Bean
	public AuthInterceptorHandler authInterceptorHandler() {
		return new AuthInterceptorHandler();
	}
}
