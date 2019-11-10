package com.kindleparser.parser.dao;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kindleparser.parser.dao.interfaces.IDAOOperations;
import com.kindleparser.parser.entities.Author;
import com.kindleparser.parser.entities.AuthorToBook;
import com.kindleparser.parser.entities.Book;
import com.kindleparser.parser.entities.Highlight;
import com.kindleparser.parser.repositories.AuthorRepository;
import com.kindleparser.parser.repositories.AuthorToBookRepository;
import com.kindleparser.parser.repositories.BookRepository;
import com.kindleparser.parser.repositories.HighlightRepository;

@Component
public class DAOOperations implements IDAOOperations{
	private static Logger log = LogManager.getLogger(DAOOperations.class.getName());
	
	@Autowired
	AuthorRepository authorRepo;
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	HighlightRepository highlightRepo;
	
	@Autowired
	AuthorToBookRepository authorToBookRepo;
	
	
	public Long getAuthorID(String authorName) {
		return authorRepo.getAuthorId(authorName);
	}
	
	
	public boolean doesAuthorExist(String authorName) {
		return authorRepo.doesAuthorExist(authorName);
	}
	
	public boolean doesAuthorToBookExist(Long authorId, Long bookId) {
		return authorToBookRepo.existsInAuthorToBookTable(authorId, bookId);
	}
	
	public List<Book> getBooksByAuthorId(Long authorId) {
		return bookRepo.findByAuthorId1(authorId);
	}
	
	
	public boolean doesBookExist(String bookTitle) {
		return bookRepo.doesBookExist(bookTitle);
	}
	
	
	public Long getBookId(String bookTitle) {
		return bookRepo.getBookId(bookTitle);
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
		log.info("Book title prior to save: " + book.getBookTitle());
		return bookRepo.save(book) != null;
	}
	
	
	public boolean saveAuthorToBookEntry(Long authorId, Long bookId) {
		AuthorToBook authorToBookEntity = new AuthorToBook(authorId, bookId);
		
		log.info("Book id: " + bookId + " Author id: " + authorId );
		if (authorToBookRepo.existsInAuthorToBookTable(authorId, bookId) || bookId == null || authorId == null ) { 
			log.info("AuthorToBook entry already exists in table or bookId or authorId is null.");
			return false;
		}
		
		authorToBookRepo.save(authorToBookEntity);
		log.info("Saved authorToBook entity");
		return true;
	}
	
	
	public boolean saveHLToDB(String contents, Long bookId) {
		Highlight highlight = new Highlight(contents, bookId);
		
		if (!highlightRepo.doesHighlightExist(contents)) {
			try {
				log.info("Attempting to insert into highlight table");
				highlight = highlightRepo.save(highlight);
			} catch (Exception ex) {
				log.error("Error occured saving highlights to highlight table with book Id: " + bookId 
						+ " \nException message: " + ex);
				log.error("Highlight contents: " + contents);
				return false;
			}
		}
	 	return true;
	}

}
