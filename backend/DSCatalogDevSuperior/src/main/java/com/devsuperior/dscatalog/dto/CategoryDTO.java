package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;

import com.devsuperior.dscatalog.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	private String name;
	private Instant createdAt;

	public CategoryDTO() {};


	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}


	 
	}
