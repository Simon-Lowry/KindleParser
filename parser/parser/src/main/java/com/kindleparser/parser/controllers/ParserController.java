package com.kindleparser.parser.controllers;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kindleparser.parser.models.HighlightsDO;
import com.kindleparser.parser.servicesInterfaces.IHLFormatter;
import com.kindleparser.parser.servicesInterfaces.IHLParser;

@RestController
@RequestMapping("/parser/")
public class ParserController {
	private static Logger log = LogManager.getLogger(ParserController.class.getName());
	
	@Autowired
	private IHLParser hlParserService;
	
	@Autowired
	private IHLFormatter hlFormatterService;
	

	@GetMapping("fullParse")
	public void parseFullHLFile() {	
		log.info("Attempting to parse Highlight file.....");
		HashMap<String, HighlightsDO> bookHighlightsMap = hlParserService.parseFullHLFile();
		bookHighlightsMap = hlFormatterService.performHLFormatting(bookHighlightsMap);
		
		log.info("File parsing and formatting has been successful.");
	}	
	
}
	

