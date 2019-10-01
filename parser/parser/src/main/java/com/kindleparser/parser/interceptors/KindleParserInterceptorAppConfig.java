package com.kindleparser.parser.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class KindleParserInterceptorAppConfig implements WebMvcConfigurer{
	@Autowired
	KindleParserInterceptor kindleParserInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(kindleParserInterceptor);
	}

}
