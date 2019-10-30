package com.kindleparser.parser.dao.interfaces;

import com.kindleparser.parser.entities.Author;
import com.kindleparser.parser.entities.Book;

public interface IDAOOperations {

	// public Author getAuthor(String authorName);
	
	public boolean doesAuthorExist(String authorName);

	public boolean saveAuthorToDB(Author author);
	
	public Long getAuthorID(String authorName);
	
	public boolean saveBookToDB(Book book);
}
