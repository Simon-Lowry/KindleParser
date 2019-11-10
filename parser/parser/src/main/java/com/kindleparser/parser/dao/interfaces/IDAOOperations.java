package com.kindleparser.parser.dao.interfaces;

import com.kindleparser.parser.entities.Author;
import com.kindleparser.parser.entities.Book;

public interface IDAOOperations {
	
	public Long getBookId(String bookTitle);
	
	public boolean doesAuthorExist(String authorName);

	public boolean saveAuthorToDB(Author author);
	
	public Long getAuthorID(String authorName);
	
	public boolean doesBookExist(String bookTitle);
	
	public boolean saveBookToDB(Book book);
	
	public boolean doesAuthorToBookExist(Long authorId, Long bookId);
	
	public boolean saveAuthorToBookEntry(Long authorId, Long bookId);
	
	public boolean saveHLToDB(String contents, Long bookId);
}
