package com.microservicos.ProductServiceAPI.controllers;

import com.microservicos.ProductServiceAPI.models.Product;
import com.microservicos.ProductServiceAPI.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.microservicos.ProductServiceAPI.models.ProductUser;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductUser>> getAllProducts() {
        List<ProductUser> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(savedProduct);
    }
}