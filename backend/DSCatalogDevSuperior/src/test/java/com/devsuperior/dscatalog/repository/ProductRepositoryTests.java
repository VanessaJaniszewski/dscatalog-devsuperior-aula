package com.devsuperior.dscatalog.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1l;
		nonExistingId = 100L;
		countTotalProducts = repository.count();
	}
	
	@Test
	public void findByIdShouldReturnOptProductWhenIdExists() {
		Optional<Product> test = repository.findById(existingId);
		Assertions.assertNotNull(test);
	}
	@Test
	public void findByIdShouldReturnNullWhenIdDoesntExist() {
		Optional<Product> test =null;
				test = repository.findById(nonExistingId);
		Assertions.assertTrue(test.isEmpty());
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementIntoDataBaseWhenNewId() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts+1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());}
	
	@Test
	public void deleteShouldThrowExcepObjectWhenIdDoesntExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
			 repository.deleteById(nonExistingId);});}
	
}


