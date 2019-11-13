package com.kindleparser.parser.models;

import java.util.ArrayList;
import java.util.List;

public class BookHLsDO {
	private String[] authors;
	private String bookTitle;
	private List<String> listOfHLContents;
	
	
	public BookHLsDO (String bookTitle) {
		this.bookTitle = bookTitle;
		listOfHLContents = new ArrayList<String>();
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
	
	public List<String> getListOfHLContents() {
		return listOfHLContents;
	}


	public void setListOfHLContents(List<String> listOfHLContents) {
		this.listOfHLContents = listOfHLContents;
	}
}
