package com.kindleparser.parser.models;

import java.util.ArrayList;
import java.util.List;

public class HighlightsDO {
	private String[] authors;
	private String bookTitle;
	private List<String> bookHighlights;
	
	
	public HighlightsDO (String bookTitle) {
		this.bookTitle = bookTitle;
		bookHighlights = new ArrayList<String>();
	}

	
	public String[] getAuthor() {
		return authors;
	}
	
	
	public void setAuthor(String[] author) {
		this.authors = author;
	}
	
	
	public String getBookTitle() {
		return bookTitle;
	}
	
	
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	
	
	public List<String> getBookHighlights() {
		return bookHighlights;
	}
	
	
	public void setBookHighlights(List<String> bookHighlights) {
		this.bookHighlights = bookHighlights;
	}
	
}
