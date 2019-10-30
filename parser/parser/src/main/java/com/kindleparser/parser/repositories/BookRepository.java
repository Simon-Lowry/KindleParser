
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
	
	 @Query("SELECT CASE WHEN COUNT(Book) > 0 THEN 'true' ELSE 'false' END FROM Book WHERE bookTitle = ?1")
	 public boolean doesBookExist(@Param("bookTitle") String bookTitle);
	
}
