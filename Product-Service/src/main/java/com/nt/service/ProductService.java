package com.nt.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nt.dto.ProductInputs;
import com.nt.entity.Product;
import com.nt.entity.ProductCategory;
import com.nt.repo.ProductCategoryRepo;
import com.nt.repo.ProductRepo;

@Service
public class ProductService {
    @Autowired
    private ProductRepo repo;

    @Autowired
    private ProductCategoryRepo catRepo;

    @Value("${app.upload.dir}")
    private String dirUrl;  // Primary directory path from properties file

    @Value("${app.additional.upload.dir}")
    private String additionalDirUrl;  // Additional directory path from properties file

    public Product saveProduct(ProductInputs prod) throws IOException {
        Product product = new Product();
        product.setName(prod.getName());
        product.setDescription(prod.getDescription());
        product.setTitle(prod.getTitle());
        product.setUnitsPrice(prod.getUnitsPrice());
        product.setUnitsStock(prod.getUnitsStock());

        // Handling image file upload
        MultipartFile imgUrl = prod.getImageFile();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            String fileName = imgUrl.getOriginalFilename();
            
            // Save to primary directory
            Path primaryDir = Paths.get(dirUrl);
            Files.createDirectories(primaryDir);  // Ensure primary directory exists
            Path primaryFilePath = primaryDir.resolve(fileName);
            Files.copy(imgUrl.getInputStream(), primaryFilePath);
            
            // Save to additional directory
            Path additionalDir = Paths.get(additionalDirUrl);
            Files.createDirectories(additionalDir);  // Ensure additional directory exists
            Path additionalFilePath = additionalDir.resolve(fileName);
            Files.copy(imgUrl.getInputStream(), additionalFilePath);

            // Set only the relative path for database storage
            product.setImageUrl("assets\\" + fileName); // Store relative path
        }

        // Handling product category
        ProductCategory category = catRepo.findByCategoryName(prod.getCategoryName());
        if (category == null) {
            category = new ProductCategory();
            category.setCategoryName(prod.getCategoryName());
            category = catRepo.save(category);  // Save and reassign new category to variable
        }

        product.setCategory(category);  // Assign category to product
        return repo.save(product);  // Save product with image path and category
    }
}
