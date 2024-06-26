package dev.arturo.mx.apirest.controller;

import dev.arturo.mx.apirest.dto.ProductDTO;
import dev.arturo.mx.apirest.service.ProductService;
import dev.arturo.mx.apirest.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/products", produces = "application/json")
    public ResponseEntity getProducts() {

        List<ProductDTO> response = productService.getAllProducts();

        return new ResponseEntity(response, HttpStatus.OK);

    }

    @PostMapping(value = "/products")
    public ResponseEntity saveProduct(@RequestBody ProductDTO productDTO) {

        productService.createProduct(productDTO);
        return new ResponseEntity(HttpStatus.CREATED);

    }

    @PutMapping(value = "/{productId}")
    public ResponseEntity updateProduct(@PathVariable int productId, @RequestBody ProductDTO productDTO) {

        ProductDTO response = productService.updateProduct(productId, productDTO);
        return new ResponseEntity(response, HttpStatus.OK);

    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity deleteProduct(@PathVariable int productId) {

        productService.deleteProduct(productId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }
}
