package com.kindleparser.parser.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Boards")
public class Board {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long boardId;
	private String name;
	private String description;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	public Long getBoardId() {
		return boardId;
	}
	
	
	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	
	public void setDescription(String description) {
		this.description = description;
	}

}
