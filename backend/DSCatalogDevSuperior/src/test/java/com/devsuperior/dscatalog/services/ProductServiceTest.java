package com.devsuperior.dscatalog.services;

import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	@Mock
	private CategoryRepository categoryRepository;
	
	
	@MockBean
	private ProductRepository repository2;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDTO;
	
	@BeforeEach
	void setup() throws Exception{
		existingId = 1L;
		nonExistingId =2L;
		dependentId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		productDTO = Factory.createProductDTO();
		category = Factory.createCategory();
		
		
		Mockito.when(repository.findAll((Pageable)any())).thenReturn(page);
		Mockito.when(repository.save(any())).thenReturn(product);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.find(any(),any(), any())).thenReturn(page);
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void findAllPagebleShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(0L, "", pageable);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findbyIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO product = service.findByID(existingId);
		Assertions.assertNotNull(product);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	public void findbyIdShouldReturnExceptionWhenIdDoesntExist() { 
		Assertions.assertThrows(ResourceNotFoundException.class, ()-> {Optional.of(service.findByID(nonExistingId));});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void updateShouldReturnDTOWhenIdExists() {
		ProductDTO productDto = service.update(existingId, Factory.createProductDTO());
		Assertions.assertNotNull(productDto);
	}
	
	@Test
	public void updateShouldReturnExceptionWhenIdDoesntExist() { 
		ProductDTO dto = new ProductDTO();
		Assertions.assertThrows(ResourceNotFoundException.class, ()->
		{Optional.of(service.update(nonExistingId, dto));});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}
	
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(()-> {service.delete(existingId);});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	
	@Test
	public void deleteShouldThrowExceptionWhenIdDoesntExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()-> {service.delete(nonExistingId);});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, ()-> {service.delete(dependentId);});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
	
	
}
