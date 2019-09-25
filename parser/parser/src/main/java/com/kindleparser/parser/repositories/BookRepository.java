
package com.kindleparser.parser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	public List<Book> findByAuthorId1(Long authorId);
	
}
