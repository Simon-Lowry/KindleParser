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
 * <p>
 * This interceptor ensures that the application only accepts requests from my host system.
 * </p>
 *
 */
@Component
public class KindleParserInterceptor implements HandlerInterceptor {
	private static Logger log = LogManager.getLogger(HLFileCopierService.class.getName());
	
	@Autowired
	Environment env;
	
	//TODO: interceptor ensures code is only run on host system.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		log.info("Request launched");
		
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			byte[] macAddressBytes = networkInterface.getHardwareAddress();
			
			if (macAddressBytes != null) {
				String macAddress = "";
			    
				for (byte b : macAddressBytes) {
					if (macAddress.length() > 1) {
						macAddress += '-';
					} 
					macAddress += String.format("%02x", b);
			    }
			
			   if (macAddress.toUpperCase().equals(env.getProperty("local.macAddress"))) {
				   log.info("Valid request");
				   return true;
			   }
			 
			}
		}
        return false;
	}
	
}
