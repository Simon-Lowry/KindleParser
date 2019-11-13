package com.kindleparser.parser.services.interfaces;

import java.util.HashMap;

import com.kindleparser.parser.models.BookHLsDO;

/**
 * <p>
 *  Parses and formats data from the highlight file for storage in DB.
 * </p>
 *
 */
public interface IHLParser {
	
	/**
	 * <p>
	 * Ingests all the data in the highlight file into a hashmap
	 * of highlight objects which are then formatted for entry into
	 * the database.
	 * </p>
	 * 
	 * @return hashmap with the results
	 */
	public  HashMap<String, BookHLsDO> parseFullHLFile();
	
	/**
	 * 
	 * @return
	 */
	public BookHLsDO getLastHighlight();
	
	/**
	 * <p>
	 *  Takes in only the new highlights from the highlight file if any exist
	 *  to be stored in the DB. Formatted and then entered into the db.
	 * </p>
	 * 
	 * @return  hashmap with the results
	 */
	public HashMap <String, BookHLsDO> parseNewHLsfromHLFile(BookHLsDO lastHighlight);
	
	
	public boolean addHighlightDOsToDB(HashMap<String, BookHLsDO> bookHighlightsMap);
	
}
