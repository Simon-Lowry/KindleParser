package com.kindleparser.parser.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Authors")
public class Author {
	@GeneratedValue(strategy  = GenerationType.IDENTITY)
    @Id
	private Long authorId;
	@NotNull
	private String authorName;
	
	public Author(String authorName) {
		this.authorName = authorName;
	}
	
	public Long getAuthorId() {
		return authorId;
	}
	
	
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	
	
	public String getAuthorName() {
		return authorName;
	}
	
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	

}
