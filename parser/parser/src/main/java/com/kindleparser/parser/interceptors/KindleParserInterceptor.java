package com.kindleparser.parser.interceptors;

import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kindleparser.parser.services.HLFileCopierService;


/**
 * 
 *
 */
@Component
public class KindleParserInterceptor implements HandlerInterceptor {
	private static Logger log = LogManager.getLogger(HLFileCopierService.class.getName());
	
	@Autowired
	Environment env;
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
	}
	
}
