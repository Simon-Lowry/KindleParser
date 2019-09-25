package com.kindleparser.parser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	public List<Author> findByAuthorName(String authorName);

}
