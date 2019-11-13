package com.kindleparser.parser.controllers;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kindleparser.parser.entities.BoardToHL;
import com.kindleparser.parser.models.BookHLsDO;
import com.kindleparser.parser.services.interfaces.IHLFormatter;
import com.kindleparser.parser.services.interfaces.IHLParser;

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
		HashMap<String, BookHLsDO> bookHighlightsMap = hlParserService.parseFullHLFile();
		
		try {
			bookHighlightsMap = hlFormatterService.performHLFormatting(bookHighlightsMap);
			hlParserService.addHighlightDOsToDB(bookHighlightsMap);
			
			
			log.info("File parsing and formatting has been successful.");
		} catch(Exception ex) {
			log.error("exception!!");
		}
		
		
	}
	
	
	@GetMapping("parseNewHighlights") 
	public void parseNewHLsFromHLFile() {
		log.info("Attempting to parse new highlights from highlight file.....");
		
		try {
			
			BookHLsDO lastHighlight = hlParserService.getLastHighlight();
			lastHighlight = hlFormatterService.formatLastHLAfterRetrieval(lastHighlight);
		    HashMap<String, BookHLsDO> bookHighlightsMap = hlParserService.parseNewHLsfromHLFile(lastHighlight); 
		    bookHighlightsMap = hlFormatterService.performHLFormatting(bookHighlightsMap);
			hlParserService.addHighlightDOsToDB(bookHighlightsMap);
			log.info("Successfully parsed new highlights from highlight file and submitted to the DB");
		} catch(Exception ex) {
			log.error("Exception occured while parsing new highlights from highlight file: " + ex);
		}	
	}
	
	
	@GetMapping("getBookHighlights/{bookTitle}")
	public void getBooksHighlight(@PathVariable("bookTitle") String bookTitle  ) {
		
	}
	
	
	@GetMapping("getAuthorsBooks/{author}") 
	public void getAuthorsBooks(@PathVariable("author") String author ) {
		
	}
	
	
	@GetMapping("getBoards")
	public void getBoards() {
		
	}
	
	
	@GetMapping("getABoardsHLs/{boardId}")
	public void getABoardsHLs(@PathVariable("boardId") Long boardId) {
		
	}
	
	
	@PostMapping("addHLToBoard")
	public void addHLToBoard(@RequestBody BoardToHL boardToHLEntity) {
		
	}
	
}
	

