package com.devsuperior.dscatalog.entities;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@EqualsAndHashCode.Include
	private Long id;
	private String name;
	
	public Category() {
	}

	
	
	}
