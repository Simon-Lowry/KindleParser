package com.kindleparser.parser.models;

public class HighlightDO {
	private Long highlightId;
	private String contents;
	
	
	public HighlightDO(Long highlightId, String contents) {
		super();
		this.highlightId = highlightId;
		this.contents = contents;
	}
	
	
	public Long getHighlightId() {
		return highlightId;
	}
	
	
	public void setHighlightId(Long highlightId) {
		this.highlightId = highlightId;
	}
	
	
	public String getContents() {
		return contents;
	}
	
	
	public void setContents(String contents) {
		this.contents = contents;
	}

}
