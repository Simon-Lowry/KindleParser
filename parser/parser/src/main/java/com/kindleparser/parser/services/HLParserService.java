package com.kindleparser.parser.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.kindleparser.parser.entities.Highlight;
import com.kindleparser.parser.models.HighlightsDO;
import com.kindleparser.parser.servicesInterfaces.IHLParser;
import com.kindleparser.parser.utilities.UtilityOperations;

@Service
public class HLParserService implements IHLParser {
	private static Logger log = LogManager.getLogger(HLFileCopierService.class.getName());
	private Scanner in;
	
	@Autowired
	Environment env;
	
	@Autowired
	private UtilityOperations utilityOps;
	private HashMap<String, HighlightsDO> bookHighlightsMap;
	private HighlightsDO lastBookHighlights;
	
	
	/**
	 * <p>
	 * Ingests all the data in the highlight file into a hashmap
	 * of highlight objects which are then formatted for entry into
	 * the database.
	 * </p>
	 * 
	 * @return true if this parsing was successful.
	 */
	public boolean parseFullHLFile() {
		bookHighlightsMap = new HashMap <String, HighlightsDO>();
		String highlightFile = findNewestHLFile();
		
		try {
			in = new Scanner(new FileReader(highlightFile));
			ingestAllHighlights();
			formatHighlights();
	//		utilityOps.displayNHighlights(bookHighlightsMap, 2);
	//		utilityOps.displaySingleBookHighlights(lastBookHighlights);
		} catch (IOException ex) {
			log.error("Error occured attempting to read from the highlights file: " + ex);
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Finds the location of the most recent highlight file saved locally from the kindle device.
	 * This will contain the most recent highlights on the kindle (the remainder are backup files).
	 * 
	 * @return the location of the most recent highlight file copied to the local system.
	 */
	public String findNewestHLFile() {
		String directoryPath = env.getProperty("local.HLFileLocation");
		File HLFileDirectory = new File(directoryPath);
		File[] filesInDirectory = HLFileDirectory.listFiles();

		Arrays.sort(filesInDirectory, Comparator.comparingLong(File::lastModified));
		return filesInDirectory[0].getAbsolutePath();
	}
	
	
	/**
	 * <p>
	 *  Gets all of the highlights, their author and book titles and 
	 *  places them in a hashmap of Highlight objects.
	 * </p>
	 * @return true if highlights were successfully mapped.
	 */
	protected boolean ingestAllHighlights() {
		HighlightsDO bookHighlights = null;
		
		while (in.hasNextLine()) {
			String bookTitleAndAuthor = in.nextLine();
			String highlightContents = getHighlightContent();
			
			if (bookHighlightsMap.containsKey(bookTitleAndAuthor)) {					// if book already added to the map
				bookHighlights = bookHighlightsMap.get(bookTitleAndAuthor);
				List<String> highlights = bookHighlights.getBookHighlights();
				highlights.add(highlightContents);
			} else {
			    bookHighlights = new HighlightsDO(bookTitleAndAuthor);
				List<String> highlights = bookHighlights.getBookHighlights();
				highlights.add(highlightContents);
				bookHighlightsMap.put(bookTitleAndAuthor, bookHighlights);
			}
			
		}
		lastBookHighlights = bookHighlights;
		
		log.info("Successfully ingested highlights, book titles, and authors from highlight file. Number of highlight objects: " + bookHighlightsMap.size() );
		in.close();
		return true;
	}
	
	
	/**
	 * <p>
	 * Writes the last highlight to a text file to be leveraged for future
	 * parsing.
	 * </p>
	 * @return - String - returns a single highlight's contents as a string.
	 */
	public boolean writeLastHighlightToFile(HighlightsDO lastHighlight) {
		try {
			PrintStream ps = new PrintStream(env.getProperty("local.lastHLFile"));
			ps.print(lastBookHighlights.getBookTitle());
			ps.print(lastBookHighlights.getAuthor());
			ps.print(lastBookHighlights.getAuthor());
		} catch(IOException ex) {
			
		}
		
		//TODO: write the last highlight file to a text file which is to be read only
		return true;
	}
	
	/**
	 * <p>
	 * Get's a single highlight's contents from the text file.
	 * </p>
	 * 
	 * @return - String - returns a single highlight's contents as a string.
	 */
	protected String getHighlightContent() {
		String highlightContents = "";
		
		while (true ) { 
			
			if (in.hasNextLine() == false) break;	
		
			String nextLine = in.nextLine();
			if (nextLine.equals("=========="))  {
				break; 
			} else {
				highlightContents += nextLine;
			}
		}
		
		return highlightContents;
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
	protected boolean formatBookTitleAndAuthor() {
		
		for (HighlightsDO bookHighlightsDo : bookHighlightsMap.values()) {	
			List<String> bookHighlights = bookHighlightsDo.getBookHighlights();
						
				
			for (int i = 0; i < bookHighlights.size(); i++) {
				String highlight = bookHighlights.get(i);
			
				//TODO: format titles and author for 6 different variations
			}
		}
		
		log.info("Book titles and Authors have been formatted in highlight object map");	
		return true;
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
	protected boolean formatHighlights() {
		
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
	
	
	public boolean parseNewHLsfromHLFile() {
		//TODO: 
		// get last highlight from last highlight file
		// use this highlight as a marker to find out where to start taking the new highlights from in the highlight file.
		// go through the highlight file find the last highlight, start taking from beyond that.
		// use same code from ingesting the other higlights
		return true;
	}
	
	
	
	
}
