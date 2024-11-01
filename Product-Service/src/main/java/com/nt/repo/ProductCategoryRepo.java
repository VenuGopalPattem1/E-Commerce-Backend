package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.nt.entity.ProductCategory;
@RepositoryRestResource(path = "productcategory")
@CrossOrigin("http://localhost:3000")
public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Long> {
	
	public ProductCategory findByCategoryName(String categoryName);
}
