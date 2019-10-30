package com.kindleparser.parser.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Book")
public class Book {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long bookId;
	private String bookTitle;
	private Long authorId1;
	private Long authorId2;
	private Long authorId3;
	
	
	public Book(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	
	
	public Book(String bookTitle, Long authorId1) {
		this.bookTitle = bookTitle;
		this.authorId1 = authorId1;
	}
	
	
	public Book(String bookTitle, Long authorId1, Long authorId2) {
		this.bookTitle = bookTitle;
		this.authorId1 = authorId1;
		this.authorId2 = authorId2;
	}
	
	
	public Book(String bookTitle, Long authorId1, Long authorId2, Long authorId3 ) {
		this.bookTitle = bookTitle;
		this.authorId1 = authorId1;
		this.authorId2 = authorId2;
		this.authorId3 = authorId3;
	}
	
	@Column(name = "bookId", nullable = false)
	public Long getBookId() {
		return bookId;
	}
	
	
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	
	
	public String getBookTitle() {
		return bookTitle;
	}
	
	
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	
	
	public Long getAuthorId1() {
		return authorId1;
	}
	
	
	public void setAuthorId1(Long authorId1) {
		this.authorId1 = authorId1;
	}
	
	
	public Long getAuthorId2() {
		return authorId2;
	}
	
	
	public void setAuthorId2(Long authorId2) {
		this.authorId2 = authorId2;
	}
	
	
	public Long getAuthorId3() {
		return authorId3;
	}
	
	public void setAuthorId3(Long authorId3) {
		this.authorId3 = authorId3;
	}

}
