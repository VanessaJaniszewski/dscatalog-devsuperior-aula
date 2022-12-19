package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = repository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
//		List<ProductDTO> listDTO = new ArrayList<>();
//		for (Product cat: list) {
//			listDTO.add(new ProductDTO(cat));}
	}
	@Transactional(readOnly =true)
	public ProductDTO findByID(Long id) {
		try {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(()-> new EntityNotFoundException("Entity not found."));
		return new ProductDTO(entity, entity.getCategories());}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("No id found.");
		}
	}
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		try{
		Product entity = new Product();
//		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTO(entity);
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Entity was not found.");
		}
	}
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
		Product entity = repository.getOne(id);
//		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTO(entity);
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
