package com.kindleparser.parser.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kindleparser.parser.models.HighlightsDO;
import com.kindleparser.parser.services.interfaces.IHLFormatter;

@Service
public class HLFormatterService implements IHLFormatter {
	private static Logger log = LogManager.getLogger(HLFormatterService.class.getName());
	
	private HashMap<String, HighlightsDO> bookHighlightsMap;
	
	public HashMap<String, HighlightsDO> performHLFormatting(HashMap<String, HighlightsDO> bookHighlightsMap) {
		this.bookHighlightsMap = bookHighlightsMap;
		formatHighlights();
		formatBookTitleAndAuthor();
		return this.bookHighlightsMap;
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
	private boolean formatBookTitleAndAuthor() {
		
		for (Map.Entry<String, HighlightsDO> entry : bookHighlightsMap.entrySet()) {	
			String authorAndTitle = entry.getKey();
			HighlightsDO highlightsDO = entry.getValue();
			
			int indexOfOpenBracket = authorAndTitle.indexOf('('); // open bracket precedes the book authors
			
			String bookTitle = (indexOfOpenBracket == -1) ?  authorAndTitle : authorAndTitle.substring(0, indexOfOpenBracket - 1);
			bookTitle = bookTitle.trim();
			String[] authors = formatAuthors(authorAndTitle, indexOfOpenBracket);
			
			highlightsDO.setBookTitle(bookTitle);
			highlightsDO.setAuthor(authors);
		}
		
		log.info("Book titles and Authors have been formatted in highlight object map");	
		return true;
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
		log.info("Authors pre formatting: " + authorsPreFormat);
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
		
		for (HighlightsDO bookHighlightsDo : bookHighlightsMap.values()) {	
			List<String> bookHighlights = bookHighlightsDo.getBookHighlights();
									
			for (int i = 0; i < bookHighlights.size(); i++) {
				String highlight = bookHighlights.get(i);
				int indexOfColon = highlight.indexOf(":", highlight.indexOf(":") + 1); // gets the index of the second semi colon
					
				highlight = highlight.substring(indexOfColon + 3);
				bookHighlights.set(i, highlight);
			}
		}
		
		log.info("Highlights have been formatted, metadata removed.");
		return true;
		
	}
	

}
