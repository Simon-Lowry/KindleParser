package com.kindleparser.parser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.AuthorToBook;

@Repository
public interface AuthorToBookRepository extends JpaRepository<AuthorToBook, Long> {
	
	@Query(value="SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM "
			+ "author_to_book WHERE author_id = ?1 AND book_id = ?2",  nativeQuery = true)
	public boolean existsInAuthorToBookTable(Long authorId, Long bookId);

}
