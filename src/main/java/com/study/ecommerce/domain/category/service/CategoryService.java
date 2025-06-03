package com.study.ecommerce.domain.category.service;

import com.study.ecommerce.domain.category.dto.req.CategoryRequest;
import com.study.ecommerce.domain.category.dto.resp.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategory(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
