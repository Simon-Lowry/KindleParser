package com.kindleparser.parser.utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kindleparser.parser.models.HighlightsDO;

@Component
public class UtilityOperations {
	private static Logger log = LogManager.getLogger(UtilityOperations.class.getName());

	public void displayNHighlights(HashMap<String, HighlightsDO> bookHLMap, int numHighlights) {
		int counter = 0;	
		
		for (HighlightsDO bookHighlightsDo : bookHLMap.values()) {
					
			log.info(bookHighlightsDo.getBookTitle() + "\n");
					
			if (counter < numHighlights) {
				counter++;
				List<String> bookHighlights = bookHighlightsDo.getBookHighlights();
						
				log.info("Highlights: ");
				for (int i = 0; i < bookHighlights.size(); i++) {
					log.info(bookHighlights.get(i) + "\n");
				}
			}  else break;
		}
	}

	
	public boolean displaySingleBookHighlights(HighlightsDO bookHighlightsDO) {
		log.info("Book title: " + bookHighlightsDO.getBookTitle() + "\n");
		log.info("Author: " + bookHighlightsDO.getAuthor());
		
		List<String> highlights = bookHighlightsDO.getBookHighlights();
		
		for (int i = 0; i < highlights.size(); i++ ) {
			log.info(highlights.get(i) + "\n");
		}
		return true;	
	}
	
	
	
	public boolean displayAllBookTitles(HashMap<String, HighlightsDO> highlightsMap) {
		
		log.info("Attempting to display all authors and titles....");
		for (Map.Entry<String, HighlightsDO> entry : highlightsMap.entrySet()) {
			HighlightsDO highlight = entry.getValue();
			
			log.info("Book title: " + highlight.getBookTitle());
		}
		
		return true;
		
	}
	
	
	public boolean writeBinaryFile() {
		try {
			log.info("Attempting to create new binary file");
			DataOutputStream  out = new DataOutputStream ( new FileOutputStream("C:\\Users\\Simon's PC\\Documents\\kindle.dat"));
			out.writeInt(59496113);
			log.info("Binary file created");
		
			out.close();
			DataInputStream in =  new DataInputStream (new FileInputStream("C:\\Users\\Simon's PC\\Documents\\kindle.dat"));
		
			log.info("Data in binary file: " + in.readInt());
			in.close();
		} catch ( IOException ex) {
			
		}
		return false;
	}

}
