package com.kindleparser.parser.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BoardToHL {
	
	@Id
	@GeneratedValue(strategy  = GenerationType.IDENTITY)
	private Long bookToHLId;
	
	private Long highlightId;
	private Long bookId;

	public Long getBookToHLId() {
		return bookToHLId;
	}
	
	
	public void setBookToHLId(Long bookToHLId) {
		this.bookToHLId = bookToHLId;
	}
	
	
	public Long getHighlightId() {
		return highlightId;
	}
	
	
	public void setHighlightId(Long highlightId) {
		this.highlightId = highlightId;
	}
	
	
	public Long getBookId() {
		return bookId;
	}
	
	
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
}
