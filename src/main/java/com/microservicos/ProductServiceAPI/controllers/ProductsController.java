package com.microservicos.ProductServiceAPI.controllers;

import com.microservicos.ProductServiceAPI.models.Product;
import com.microservicos.ProductServiceAPI.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.microservicos.ProductServiceAPI.dtos.ProductDTO;
import com.microservicos.ProductServiceAPI.models.ProductUser;
import java.util.List;
import java.net.URI;
import jakarta.validation.Valid;
import com.microservicos.ProductServiceAPI.errors.ProductValidationError;
import com.microservicos.ProductServiceAPI.errors.ErrorModelView;
import org.springframework.http.HttpStatus;
import com.microservicos.ProductServiceAPI.dtos.ProductUserDTO;
import com.microservicos.ProductServiceAPI.model_views.Message;

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
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO product) {
        try {
            Product savedProduct = productService.createProduct(product);
            return ResponseEntity.created(URI.create("/products/" + savedProduct.getId())).body(savedProduct);
        } catch (ProductValidationError e) {
            return ResponseEntity.badRequest().body(new ErrorModelView(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorModelView("Erro interno do servidor."));
        }
    }

    @PostMapping("/with-user")
    public ResponseEntity<?> createProductWithUser(@RequestBody @Valid ProductUserDTO dto) {
        try {
            boolean success = productService.createProductWithUserKafka(dto);
            if(success) {
                return ResponseEntity.ok(new Message("Agendamento de criação de produto realizado com sucesso."));
            } else {
                return ResponseEntity.badRequest().body(new ErrorModelView("Erro ao agendar criação de produto."));
            }
        } catch (ProductValidationError e) {
            return ResponseEntity.badRequest().body(new ErrorModelView(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorModelView("Erro interno do servidor."));
        }
    }
}