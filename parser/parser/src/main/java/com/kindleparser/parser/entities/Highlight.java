package com.kindleparser.parser.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Highlights")
public class Highlight {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long highlightId;
	private Long bookId;
	private String contents;

	
	@Column(name = "highlightId", nullable = false)
	public Long getHighlightId() {
		return highlightId;
	}
	
	
	public void setHighlightId(Long highlightId) {
		this.highlightId = highlightId;
	}
	
	
	@Column(name = "bookId", nullable = false)
	public Long getBookId() {
		return bookId;
	}
	
	
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	
	
	@Column(name = "contents", nullable = false)
	public String getContents() {
		return contents;
	}
	
	
	public void setContents(String contents) {
		this.contents = contents;
	}

}
