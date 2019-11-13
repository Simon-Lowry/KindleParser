package com.kindleparser.parser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.Highlight;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, Long> {

	public List<Highlight> findByBookId(Long bookId);
	
	
	@Query(value="SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM Highlights WHERE contents = ?1", nativeQuery = true)
	public boolean doesHighlightExist(String contents);
	
		
	@Query(value="SELECT COUNT(*) FROM Highlights WHERE book_id = ?1", nativeQuery = true)
	public int getNumHLForBook(Long bookId);
	
	// TODO: get all highlights for an author
	// author id = 1
	
	// get a list of all the book an author has written from AuthorToBookRepo
	// sql get all book ids for an author
	// select * from highlighghts
	
	
	// TODO: get all highlights for a book
	
	// TODO: get all highlights for a board
	
}
