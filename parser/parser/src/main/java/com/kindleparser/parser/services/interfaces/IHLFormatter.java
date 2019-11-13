package com.kindleparser.parser.services.interfaces;

import java.util.HashMap;

import com.kindleparser.parser.models.BookHLsDO;

public interface IHLFormatter {
	
	public HashMap<String, BookHLsDO> performHLFormatting(HashMap<String, BookHLsDO> bookHighlightsMap) throws Exception;

	public BookHLsDO formatLastHLAfterRetrieval(BookHLsDO lastHighlight);
}
