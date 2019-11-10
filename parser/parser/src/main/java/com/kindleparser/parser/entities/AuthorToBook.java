package com.kindleparser.parser.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AuthorToBook")
public class AuthorToBook {

	@Id
	@GeneratedValue(strategy  = GenerationType.IDENTITY)
	private Long authorToBookId;
	
	private Long authorId;
	private Long bookId;
	
	public AuthorToBook(Long authorId, Long bookId) {
		this.authorId = authorId;
		this.bookId = bookId;
	}
	
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

}
