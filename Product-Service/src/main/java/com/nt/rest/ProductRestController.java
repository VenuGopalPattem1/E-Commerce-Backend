package com.nt.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nt.dto.ProductInputs;
import com.nt.entity.Product;
import com.nt.service.ProductService;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class ProductRestController {

    @Autowired
    private ProductService ser;

    @PostMapping(value = "/saveProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveImage(
    		   @RequestParam("name") String name,
    	        @RequestParam("description") String description,
    	        @RequestParam("title") String title,
    	        @RequestParam("unitsPrice") int unitsPrice,
    	        @RequestParam("unitsStock") int unitsStock,
    	        @RequestParam("categoryName") String categoryName,
    	        @RequestPart("imageFile") MultipartFile imageFile) {

        ProductInputs inputs = new ProductInputs();
        inputs.setImageFile(imageFile);
        inputs.setName(name);
        inputs.setDescription(description);
        inputs.setTitle(title);
        inputs.setUnitsPrice(unitsPrice);
        inputs.setUnitsStock(unitsStock);
        inputs.setCategoryName(categoryName);

        try {
            Product prod = ser.saveProduct(inputs);
            return new ResponseEntity<>(prod, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while saving the product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
