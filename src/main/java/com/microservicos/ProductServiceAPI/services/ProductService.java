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
import com.microservicos.ProductServiceAPI.dtos.ProductDTO;
import com.microservicos.ProductServiceAPI.errors.ProductValidationError;
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
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Product createProduct(ProductDTO product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new ProductValidationError("Nome do produto é obrigatório");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new ProductValidationError("Preço do produto deve ser maior que 0");
        }
        if (product.getQuantity() == null || product.getQuantity() <= 0) {
            throw new ProductValidationError("Quantidade do produto deve ser maior que 0");
        }
        if (product.getUserId() == null) {
            throw new ProductValidationError("ID do usuário é obrigatório");
        }

        UserClient user = userClientService.getUserById(product.getUserId());
        if (user == null) {
            throw new ProductValidationError("Usuário não encontrado");
        }

        Product newProduct = new Product(
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getQuantity(),
            product.getUserId()
        );
        return productRepository.save(newProduct);
    }
}