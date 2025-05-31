package com.microservicos.ProductServiceAPI.repositories;

import com.microservicos.ProductServiceAPI.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}