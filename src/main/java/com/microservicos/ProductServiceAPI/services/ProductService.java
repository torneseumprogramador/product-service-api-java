package com.microservicos.ProductServiceAPI.services;

import com.microservicos.ProductServiceAPI.models.Product;
import com.microservicos.ProductServiceAPI.repositories.ProductRepository;
import com.microservicos.ProductServiceAPI.models.ProductUser;
import com.microservicos.ProductServiceAPI.models.UserClient;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserClientService userClientService;

    public List<ProductUser> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<Long> userIds = products.stream()
            .map(Product::getUserId)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
        Map<Long, UserClient> usersMap = userClientService.getUsersByIds(userIds);
        return products.stream()
            .map(product -> new ProductUser(
                product,
                usersMap.get(product.getUserId())
            ))
            .collect(Collectors.toList());
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}