package com.kindleparser.parser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.Highlight;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, Long> {

	public List<Highlight> findByBookId(Long bookId);
}
