package com.kindleparser.parser.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.kindleparser.parser.dao.interfaces.IDAOOperations;
import com.kindleparser.parser.entities.Author;
import com.kindleparser.parser.entities.Book;
import com.kindleparser.parser.entities.Highlight;
import com.kindleparser.parser.repositories.AuthorRepository;
import com.kindleparser.parser.repositories.BookRepository;
import com.kindleparser.parser.repositories.HighlightRepository;

public class DAOOperations implements IDAOOperations{
	@Autowired
	AuthorRepository authorRepo;
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	HighlightRepository highlightRepo;
	
	
	public Long getAuthorID(String authorName) {
		List<Author> authorList =  authorRepo.findByAuthorName(authorName);
		
		return (authorList == null) ? null : authorList.get(0).getAuthorId();
	}
	
	public boolean doesAuthorExist(String authorName) {
		List<Author> authorList =  authorRepo.findByAuthorName(authorName);
		
		return (authorList == null) ? true : false;
	}
	
	
	public List<Book> getBooksByAuthorId(Long authorId) {
		return bookRepo.findByAuthorId1(authorId);
	}
	
	
	public boolean doesBookExist(String bookTitle) {
		return bookRepo.doesBookExist(bookTitle);
	}
	
	
	public List<Highlight> getHighlightsByBookId(Long bookId) {
		return highlightRepo.findByBookId(bookId);
	}
	
	
	public Highlight getHighlight(Long highlightId) {
		return highlightRepo.getOne(highlightId);
	}
	
	
	public boolean saveHighlightToDB(Highlight highlight) {
		return highlightRepo.save(highlight) != null;
	}
	
	
	public boolean saveAuthorToDB(Author author) {
		return authorRepo.save(author) != null;
	}
	
	
	public boolean saveBookToDB(Book book) {
		return bookRepo.save(book) != null;
	}
	
}
