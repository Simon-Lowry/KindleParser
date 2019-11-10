package com.kindleparser.parser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	@Query(value ="SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM authors WHERE author_name = ?1", nativeQuery = true)
	public boolean doesAuthorExist(String authorName);
	
	
	@Query(value = "SELECT author_id FROM Authors WHERE author_name = ?1", nativeQuery = true)
	public Long getAuthorId(@Param("author_name") String authorName);

}
