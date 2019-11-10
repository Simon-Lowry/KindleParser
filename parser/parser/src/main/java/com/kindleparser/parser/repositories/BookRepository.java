
package com.kindleparser.parser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	public List<Book> findByAuthorId1(Long authorId);
	
	 @Query("SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM Book WHERE book_title = ?1")
	 public boolean doesBookExist(@Param("bookTitle") String bookTitle);
	 
	 
	 @Query(value = "SELECT book_id FROM Book WHERE book_title = ?1", nativeQuery = true)
	 public Long getBookId(@Param("bookTitle") String bookTitle);
	
}
