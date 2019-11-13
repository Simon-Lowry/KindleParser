package com.kindleparser.parser.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kindleparser.parser.models.BookHLsDO;
import com.kindleparser.parser.services.interfaces.IHLFormatter;
import com.kindleparser.parser.utilities.UtilityOperations;

@Service
public class HLFormatterService implements IHLFormatter {
	private static Logger log = LogManager.getLogger(HLFormatterService.class.getName());
	
	private HashMap<String, BookHLsDO> bookHighlightsMap;
	
	private UtilityOperations utils = new UtilityOperations();
	
	public HashMap<String, BookHLsDO> performHLFormatting(HashMap<String, BookHLsDO> bookHighlightsMap){
		log.info("Beginning formatting....");
		this.bookHighlightsMap = bookHighlightsMap;
		formatHighlights();
		formatBookTitlesAndAuthors();
		utils.displayAllBookTitles(bookHighlightsMap);
		return this.bookHighlightsMap;
	}
	
	
	/**
	 * For adding new highlights to the DB, the last highlight
	 * is stored in a text file. This file is to be formatted correctly after retrieval
	 * @param lastHighlight
	 * @return the last highlight after it's been properly formatted.
	 */
	public BookHLsDO formatLastHLAfterRetrieval(BookHLsDO lastHighlight) {
		String bookTitle = lastHighlight.getBookTitle();
		log.info("Starting formatting process for last highlight.");
		int indexOfOpenBracket = bookTitle.indexOf('('); // open bracket precedes the book authors
		bookTitle = formatBookTitle(bookTitle, indexOfOpenBracket);
		lastHighlight.setBookTitle(bookTitle);
		
		String lastHLContents = lastHighlight.getListOfHLContents().get(0);
		
		lastHLContents = formatAHighlight(lastHLContents);
		
		log.info("Book title after formatting: " + bookTitle);
		log.info("Highlight after formatting: " + lastHLContents);
		
		return lastHighlight;

	}
	
	
	/**
	 * <p>
	 * Authors appear in 6 different variations in the highlight file.
	 * This method formats both the book title and author into a palatable
	 * format for the DB.
	 *  </p>
	 *  
	 * @param highlightsMap - contains all of the highlights including book title and author
	 * @return true to confirm the action was completed
	 */
	private boolean formatBookTitlesAndAuthors() {
		log.info("Bgininning book and title formatting");
		for (Map.Entry<String, BookHLsDO> entry : bookHighlightsMap.entrySet()) {	
			String authorAndTitle = entry.getKey();
			BookHLsDO highlightsDO = entry.getValue();
			
			int indexOfOpenBracket = authorAndTitle.indexOf('('); // open bracket precedes the book authors
			String bookTitle = formatBookTitle(authorAndTitle, indexOfOpenBracket);
			String[] authors = formatAuthors(authorAndTitle, indexOfOpenBracket);
			
			highlightsDO.setBookTitle(bookTitle);
			highlightsDO.setAuthor(authors);
		}
		
		log.info("Book titles and Authors formatting completed");	
		return true;
	}
	
	
	public String formatBookTitle(String authorAndBookTitle, int indexOfOpenBracket) {
		String regex = "[^A-Za-z0-9 _.,!\"'$?@%:-=+ ]*";									
		String bookTitle = (indexOfOpenBracket == -1) ?  authorAndBookTitle : authorAndBookTitle.substring(0, indexOfOpenBracket - 1);
		bookTitle = bookTitle.trim();
		bookTitle = bookTitle.replaceAll(regex, "");
		log.info("Book title after normalizing data: " + bookTitle );
		
		return bookTitle;
	}
	
	
	private String[] formatAuthors(String authorAndTitle, int indexOfOpenBracket) {
		String[] authors = null;
		
		
		if (indexOfOpenBracket == -1) {
			authors = new String[1];
			authors[0] = "Unknown";
			return authors;
		}
				
		int indexOfCloseBracket = authorAndTitle.indexOf(')');
		
		String authorsPreFormat = authorAndTitle.substring(indexOfOpenBracket + 1, indexOfCloseBracket );
		int numCommas = (int) authorsPreFormat.chars().filter(ch -> ch == ',').count();			// commas can be used as separators for multiple authors
		int numSemiColons = (int) authorsPreFormat.chars().filter(ch -> ch == ';').count();		// semi-colons can be used as seprates for multiple authors
			
		if (numSemiColons > 0 || numCommas > 1 ) { // separate authors by semi-colons or commas
			if (numSemiColons > 0) {	// name1; name2 etc
				authors = authorsPreFormat.split(";");
		
			} else { 					// multiple commas, commas used to separate authors
				authors = separateAuthorsByColon(authorsPreFormat);
			}	
		} else {
			authors = new String[1];
			authors[0] = authorsPreFormat;
		}		
		authors = fixAuthorNamingStructure(authors);
		log.info("Authors have been extracted and correctly formatted for book");
		return authors;
	}

	
	/**
	 * Multiple authors can be separated by colons in the author string.
	 * This method separates them out into an array of authors
	 * 
	 * @param authorsPreFormat
	 * @return array of authors
	 */
	private String[] separateAuthorsByColon (String authorsPreFormat) {
		int commaCount = 0;
		int nameStartIndex = 0;
		
		List<String> authorsList = new ArrayList<String>();
		int indexOfNameEnd = authorsPreFormat.indexOf(",");
		while (indexOfNameEnd != -1 ) {
			commaCount++;
			
			if (commaCount == 2) {
				String extractedAuthor = authorsPreFormat.substring(nameStartIndex, indexOfNameEnd).trim();
				authorsList.add(extractedAuthor);
				indexOfNameEnd++;
				nameStartIndex = indexOfNameEnd;
				commaCount = 0;
			}
			indexOfNameEnd = authorsPreFormat.indexOf(",", indexOfNameEnd + 1);
		}
		String lastAuthor = authorsPreFormat.substring(nameStartIndex).trim();
		authorsList.add(lastAuthor);
		
		log.info("list size: " + authorsList.size());
		
		String[] array = new String[authorsList.size()]; // convert to an array
		authorsList.toArray(array); 

		
		return array;
	}
	
	
	/**
	 * Author names can be in the wrong format with the last name followed by a colon and then the first name
	 * This method
	 * 
	 * @return returns the author's names in the correct format in an array of strings
	 */
	private String[] fixAuthorNamingStructure(String[] authors) {
		
		for (int i = 0; i < authors.length; i++) {
			String name = authors[i];
			
		
			if (name.contains(",")) {
				int indexOfComma = name.indexOf(",");
				String firstName = name.substring(indexOfComma + 2);
				String lastName = name.substring(0, indexOfComma);
				authors[i] = firstName + " " + lastName;
				
			}
			authors[i] = authors[i].trim();   // ensure preceding or post name white space is removed
		}
		log.info("Completed name re-structuring for authors");
		return authors;
	}
	
	
	/**
	 * <p>
	 * Highlights appear in with additional metadata not required for this application.
	 * This method strips that metadata leaving only the highlight contents.
	 * </p>
	 * 
	 * @param highlightsMap
	 * @return returns true if the formatting was successful.
	 */
	private boolean formatHighlights() {
		log.info("Bgininning highlight formatting");
		for (BookHLsDO bookHighlightsDo : bookHighlightsMap.values()) {	
			List<String> bookHighlights = bookHighlightsDo.getListOfHLContents();
									
			for (int i = 0; i < bookHighlights.size(); i++) {
				String hlContents = bookHighlights.get(i);
				hlContents = formatAHighlight(hlContents);
				bookHighlights.set(i, hlContents);
			}
		}
		
		log.info("Highlights have been formatted, metadata removed.");
		return true;
		
	}
	
	
	private String formatAHighlight(String hlContents) {
		String regex = "[^A-Za-z0-9 _.,!\"'$?@%:-=+ ]*";
		int indexOfColon = hlContents.indexOf(":", hlContents.indexOf(":") + 1); // gets the index of the second semi colon
		
		hlContents = hlContents.substring(indexOfColon + 3);
		hlContents = hlContents.replaceAll(regex, "");
		return hlContents;
	}

}
