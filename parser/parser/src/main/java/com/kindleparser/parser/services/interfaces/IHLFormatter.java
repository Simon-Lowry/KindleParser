package com.kindleparser.parser.services.interfaces;

import java.util.HashMap;

import com.kindleparser.parser.models.HighlightsDO;

public interface IHLFormatter {
	
	public HashMap<String, HighlightsDO> performHLFormatting(HashMap<String, HighlightsDO> bookHighlightsMap);

}
