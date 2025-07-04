package com.study.ecommerce.domain.product.repository;

import com.study.ecommerce.domain.product.entity.Product;
import com.study.ecommerce.domain.product.entity.Product.ProductStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> , ProductQueryRepository{


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long productId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdWithOptimisticLock(@Param("id") Long productId);

    List<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus productStatus);

    Page<Product> findByStatus(ProductStatus productStatus, Pageable pageable);

    Optional<Product> findByIdAndStatus(Long id, ProductStatus productStatus);

    Page<Product> findByNameContainingIgnoreCaseAndStatus(String keyword, ProductStatus productStatus, Pageable pageable);

    Page<Product> findByPriceBetweenAndStatus(Long minPrice, Long maxPrice, ProductStatus productStatus, Pageable pageable);

    Page<Product> findByCategoryIdAndNameContainingIgnoreCaseAndStatus(Long categoryId, String keyword, ProductStatus productStatus, Pageable pageable);
}
