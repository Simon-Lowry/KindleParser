package com.kindleparser.parser.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Highlights")
public class Highlight {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long highlightId;
	
	@NotNull
	private Long bookId;
	
	@NotNull
	@Column(length=4000)
	private String contents;

	public Highlight (String contents, Long bookId) {
		this.contents = contents;
		this.bookId = bookId;
	}
	
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
