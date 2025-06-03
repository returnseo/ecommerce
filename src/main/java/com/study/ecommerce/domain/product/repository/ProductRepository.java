package com.study.ecommerce.domain.product.repository;

import com.study.ecommerce.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
