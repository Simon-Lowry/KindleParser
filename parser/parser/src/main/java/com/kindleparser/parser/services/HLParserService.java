package com.kindleparser.parser.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.kindleparser.parser.dao.interfaces.IDAOOperations;
import com.kindleparser.parser.entities.Author;
import com.kindleparser.parser.entities.AuthorToBook;
import com.kindleparser.parser.entities.Book;
import com.kindleparser.parser.models.HighlightsDO;
import com.kindleparser.parser.services.interfaces.IHLFormatter;
import com.kindleparser.parser.services.interfaces.IHLParser;
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
	
	@Autowired
	private IDAOOperations daoOperations;
	
	private final String END_OF_HIGHLIGHT = "==========";
	
	
	/**
	 * <p>
	 * Ingests all the data in the highlight file into a hashmap
	 * of highlight objects which are then formatted for entry into
	 * the database.
	 * </p>
	 * 
	 * @return true if this parsing was successful.
	 */
	public HashMap <String, HighlightsDO> parseFullHLFile() {
		bookHighlightsMap = new HashMap <String, HighlightsDO>();
		String highlightFile = findNewestHLFile();
		
		try {
			in = new Scanner(new FileReader(highlightFile));
			ingestAllHighlights();
			writeLastHighlightToFile(lastBookHighlights);
		} catch (IOException ex) {
			log.error("Error occured attempting to read from the highlights file: " + ex);
			
		}
		return bookHighlightsMap;
	}
	
	
	
	/**
	 * Finds the location of the most recent highlight file saved locally from the kindle device.
	 * This will contain the most recent highlights on the kindle (the remainder are backup files).
	 * 
	 * @return the location of the most recent highlight file copied to the local system.
	 */
	private String findNewestHLFile() {
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
	private boolean ingestAllHighlights() {
		HighlightsDO bookHighlights = null;
		String highlightContents = null;
		String bookTitleAndAuthor= null;
		
		while (in.hasNextLine()) {
			bookTitleAndAuthor = in.nextLine();
			highlightContents = getHighlightContent();
			
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
		lastBookHighlights = new HighlightsDO(bookTitleAndAuthor);
		List<String> lastHLContents = new ArrayList<String>();
		lastHLContents.add(highlightContents);
		lastBookHighlights.setBookHighlights(lastHLContents);
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
	private boolean writeLastHighlightToFile(HighlightsDO lastHighlight) {
		try {
			PrintStream ps = new PrintStream(env.getProperty("local.lastHLFile"));
			ps.println(lastBookHighlights.getBookTitle());
			ps.println(lastBookHighlights.getBookHighlights());
			ps.close();
		} catch(IOException ex) {
			log.error("Error Writing to last highlight file: " + ex);
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
	private String getHighlightContent() {
		String highlightContents = "";
		
		while (true) { 
			
			if (in.hasNextLine() == false)  // end of file
				break;	
		
			String nextLine = in.nextLine();
			if (nextLine.equals(END_OF_HIGHLIGHT))  {	// end of a highlight is denoted by ==========
				break; 
			} else {
				highlightContents += nextLine;
			}
		}
		
		return highlightContents;
	}

	
	public HashMap <String, HighlightsDO> parseNewHLsfromHLFile() {
		//TODO: 
		// get last highlight from last highlight file
		try {
			in = new Scanner(new FileReader(env.getProperty("local.lastHLFile")));
		//	writeLastHighlightToFile(lastBookHighlights);
		} catch(IOException ex) {
			log.error("Exception occured attempting to read from last highlight file: " + ex);
		}
		
		// set location of scanner to the last highlight
		while (in.hasNextLine()) {
			
		}
		// use this highlight as a marker to find out where to start taking the new highlights from in the highlight file.
		// go through the highlight file find the last highlight, start taking from beyond that.
		// use same code from ingesting the other higlights
		return null;
	}
	
	
	public boolean addHighlightsToDB(HashMap<String, HighlightsDO> bookHighlightsMap) {
		try {
		for (Map.Entry<String, HighlightsDO> entry : bookHighlightsMap.entrySet()) {
			HighlightsDO highlightsDO = entry.getValue();
			String[] authors = highlightsDO.getAuthor();
			String bookTitle = highlightsDO.getBookTitle();
			
			saveAuthorsToDB(authors);
			saveBookToDB(authors, bookTitle);
			Long bookId = daoOperations.getBookId(bookTitle);
			saveToAuthorsToBookTable(authors, bookId);
			saveHLContentsToDB(highlightsDO.getBookHighlights(), bookId);
		}
		
		} catch(Exception ex) {
			log.error("BRRRRRRRROKEN");
		}

		return true;
	}
	
	
	private boolean saveAuthorsToDB(String[] authors) {
		
		for (int i = 0; i < authors.length; i++) {
			String authorName = authors[i];
			
			if (!daoOperations.doesAuthorExist(authorName)) {
				log.info("Author not currently entered into the DB. Attempting to save author " + authorName + " to DB");
				Author author = new Author(authorName);
				daoOperations.saveAuthorToDB(author);
				log.info("Successfully saved author " + authorName + " to DB");
			} else {
				log.info("Failed saving author " + authorName + " to DB. Author already exists in the DB.");
			}
		}
		
		return true;
	}
	
	
	private boolean saveBookToDB(String[] authors, String bookTitle) throws Exception {
		int numAuthors = authors.length;
		Long author1;
		Long author2;
		Long author3;
		Book book = null;
		
		if (authors == null || authors.length == 0 || daoOperations.doesBookExist(bookTitle) )
			return false;
		
		switch(numAuthors) {
			case 1:
				author1 = daoOperations.getAuthorID(authors[0]);
				book = new Book(bookTitle, author1);
				break;
			case 2: 
				author1 = daoOperations.getAuthorID(authors[0]);
			    author2 = daoOperations.getAuthorID(authors[1]);
			    book = new Book(bookTitle, author1, author2);
				break;
			default: 	// for three or more authors
				author1 = daoOperations.getAuthorID(authors[0]);
				author2 = daoOperations.getAuthorID(authors[1]);
				author3 = daoOperations.getAuthorID(authors[2]);
				book = new Book(bookTitle, author1, author2, author3);
				break;
		}
		
		log.info("Authors prior book saving: " + book.getAuthorId1());
	
		daoOperations.saveBookToDB(book);
		return true;
	}
	
	
	private boolean saveToAuthorsToBookTable(String[] authors, Long bookId) {
		try {
			for (String author : authors) {
				Long authorId = daoOperations.getAuthorID(author);
				daoOperations.saveAuthorToBookEntry(authorId, bookId);	
			}
		} catch (Exception ex) {
			log.error("Error occured saving entry to authorToBook table with book Id: " + bookId 
					+ " \nException message: " + ex);
			return false;
		}
		return true;
	}
	
	
	private boolean saveHLContentsToDB(List<String> highlightContents, Long bookId) {
		try {
			for (String contents : highlightContents) {
				boolean saveSuccess = daoOperations.saveHLToDB(contents, bookId);
				
				if (!saveSuccess)  {
					log.error("Error occured saving highlights to highlight table with book Id: " + bookId);
					log.error("Highlight contents: " + contents);
					return false;
				}
			}
			
		} catch (Exception ex) {
			log.error("Error occured saving highlights to highlight table with book Id: " + bookId 
					+ " \nException message: " + ex);
			return false;
		}
		return true;
	}
}
