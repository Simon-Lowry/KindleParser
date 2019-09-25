package com.kindleparser.parser;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContainer implements WebServerFactoryCustomizer< ConfigurableWebServerFactory >  {
	
	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		// TODO Auto-generated method stub
		factory.setPort(8082);
	}

}