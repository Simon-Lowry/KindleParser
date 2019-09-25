package com.kindleparser.parser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kindleparser.parser.entities.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
