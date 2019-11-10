package com.kindleparser.parser.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kindleparser.parser.services.interfaces.IHLFileCopier;


/**
 *
 * This controller is used for testing purposes. Eventually will be replaced by a windows service 
 * automatically running this code.
 *
 */
@RestController
@RequestMapping("/service/")
public class HLServiceController {
	private static Logger log = LogManager.getLogger(HLServiceController.class.getName());
	
	@Autowired
	private IHLFileCopier hlFileCopierService;
	
	
	@GetMapping
	public void performServiceWork() {
		
		hlFileCopierService.isKindleConnected();
	}

}
