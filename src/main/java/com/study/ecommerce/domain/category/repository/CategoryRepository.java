package com.study.ecommerce.domain.category.repository;

import com.study.ecommerce.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId")
    Long countProductsByCategory(@Param("categoryId") Long id);
}
