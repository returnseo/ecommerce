package com.study.ecommerce.domain.product.service;

import com.study.ecommerce.domain.category.entity.Category;
import com.study.ecommerce.domain.category.repository.CategoryRepository;
import com.study.ecommerce.domain.product.dto.resp.ProductResponse;
import com.study.ecommerce.domain.product.entity.Product;
import com.study.ecommerce.domain.product.repository.ProductRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.study.ecommerce.domain.product.entity.Product.ProductStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     *  카테고리별 판매중인 상품을 전체조회
     *  List<ProductResponse> getActiveProductsByCategory
     *  param Long categoryId
     */
    public List<ProductResponse> getActiveProductsByCategory(Long categoryId) {
        // 카테고리의 존재
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        // 해당 카테고리의 판매중인 상품을 찾는다.
        List<Product> products = productRepository.findByCategoryIdAndStatus(categoryId, ACTIVE);

        return products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        product.getStatus(),
                        category.getName()
                ))
                .toList();
    }

    /**
     * 페이징 모든 판매중인 상품 조회 -> pageable
     * Page<ProductResponse> getAllActiveProducts
     */
    public Page<ProductResponse> getAllActiveProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByStatus(ACTIVE, pageable);

        if (products.isEmpty()) {
            return products.map(product -> new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getStatus(),
                    "분류 없음"
            ));
        }

        // 카테고리정보를 조회, 효율적으로 조회를 하기위해서 저는 맵생성
        List<Long> categoryIds = products.getContent().stream()
                .map(Product::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, String> categoryMap = new HashMap<>();
        if (!categoryIds.isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            categories.forEach(category ->
                    categoryMap.put(category.getId(), category.getName()));
        }

        Page<ProductResponse> result = products.map(product -> {
            String categoryName = "분류 없음";
            if (product.getCategoryId() != null) {
                categoryName = categoryMap.getOrDefault(product.getCategoryId(), "분류 없음");
            }

            return new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getStatus(),
                    categoryName
            );
        });

        return result;
    }

    /**
     * 판매중인 상품 상세조회 -> id, ProductResponse
     */
    public ProductResponse getActiveProduct(Long id) {
        Product product = productRepository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("판매중인 상품을 찾을 수 없습니다."));

        String categoryName = "분류 없음";
        if (product.getCategoryId() != null) {
            Category category = categoryRepository.findById(product.getCategoryId())
                    .orElse(null);

            if (category != null) {
                categoryName = category.getName();
            }
        }

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                categoryName
        );
    }

    /**
     * 상품명으로 판매중인 상품을 검색
     * Page<ProductResponse>
     * param Pageable, String keyword
     */
    public Page<ProductResponse> searchActiveProducts(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseAndStatus(keyword, ACTIVE, pageable);
        return mapToProductResponse(products);
    }

    /**
     * 가격 범위로 판매중인 상품 검색
     * Long minPrice, Long maxPrice
     */
    public Page<ProductResponse> getActiveProductsByPriceRange(Long minPrice, Long maxPrice, Pageable pageable) {
        Page<Product> products = productRepository.findByPriceBetweenAndStatus(minPrice, maxPrice, ACTIVE, pageable);
        return mapToProductResponse(products);
    }

    /**
     *  카테고리 내에서 상품명으로 검색
     *  categoryId, keyword
     */
    public Page<ProductResponse> searchActiveProductsInCategory(Long categoryId, String keyword, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        Page<Product> products =
                productRepository.findByCategoryIdAndNameContainingIgnoreCaseAndStatus(categoryId, keyword, ACTIVE, pageable);

        return products.map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                category.getName()
        ));
    }


    /**
     *  extract method
     *  Product Page를 ProductResponse의 Page 변환하는 공통 메소드 구현
     *  Page<Product> -> Page<ProductResponse>
     */
    private Page<ProductResponse> mapToProductResponse(Page<Product> products) {
        List<Long> categoryIds = products.getContent().stream()
                .map(Product::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, String> categoryMap = new HashMap<>();
        if (!categoryIds.isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            categories.forEach(category ->
                    categoryMap.put(category.getId(), category.getName()));
        }

        return products.map(product -> {
            String categoryName = "분류 없음";
            if (product.getCategoryId() != null) {
                categoryName = categoryMap.getOrDefault(product.getCategoryId(), "분류 없음");
            }

            return new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getStatus(),
                    categoryName
            );
        });

    }



}
