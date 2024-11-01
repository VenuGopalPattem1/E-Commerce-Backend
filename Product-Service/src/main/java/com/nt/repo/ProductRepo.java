package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.nt.entity.Product;
import java.util.List;

@RepositoryRestResource(path = "product")
@CrossOrigin
public interface ProductRepo extends JpaRepository<Product, Long> {
    
    // Returns a list of products by category ID
    List<Product> findByCategory_CategoryId(@Param("id") Long id);
    
    // Returns a list of products containing the specified name
    List<Product> findByNameContaining(@Param("name") String name);
}
