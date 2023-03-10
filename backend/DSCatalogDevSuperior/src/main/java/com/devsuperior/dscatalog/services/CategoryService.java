package com.devsuperior.dscatalog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable){
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> new CategoryDTO(x));
//		List<CategoryDTO> listDTO = new ArrayList<>();
//		for (Category cat: list) {
//			listDTO.add(new CategoryDTO(cat));}
	}
//	
//	@Transactional(readOnly = true)
//	public List<CategoryDTO> findAll(){
//		List<Category> list = repository.findAll();
//		List<CategoryDTO> listDTO = new ArrayList<>();
//		for (Category cat: list) {
//			listDTO.add(new CategoryDTO(cat));}
//	return listDTO; 
//	}
	
	
	@Transactional(readOnly =true)
	public CategoryDTO findByID(Long id) {
		try {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(()-> new EntityNotFoundException("Entity not found."));
		return new CategoryDTO(entity);}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("No id found.");
		}
	}
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		try{
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Entity was not found.");
		}
	}
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity = repository.getOne(id);
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Entity was not found.");
		}
	}
	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+id);
		}catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation.");
		}
	}

	
}
