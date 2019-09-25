package com.kindleparser.parser.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kindleparser.parser.servicesInterfaces.IHLFileCopier;
import com.kindleparser.parser.servicesInterfaces.IHLParser;

@RestController
@RequestMapping("/parser/")
public class ParserController {
	private static Logger log = LogManager.getLogger(ParserController.class.getName());
	
	@Autowired
	private IHLFileCopier hlFileCopierService;
	
	@Autowired
	private IHLParser hlParserService;
	

	@GetMapping("fullParse")
	public void parseFullHLFile() {	
		log.info("Attempting to parse Highlight file.....");
		hlParserService.parseFullHLFile();
	}	
	
}
	

