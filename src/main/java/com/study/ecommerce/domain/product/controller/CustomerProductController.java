package com.study.ecommerce.domain.product.controller;

import com.study.ecommerce.domain.product.dto.resp.ProductResponse;
import com.study.ecommerce.domain.product.service.CustomerProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class CustomerProductController {
    private final CustomerProductService customerProductService;

    /**
     * 카테고리별 상품 조회 (판매중인 상품만)
     */
    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            log.info("카테고리별 상품 조회 요청: categoryId={}", categoryId);
            List<ProductResponse> products = customerProductService.getActiveProductsByCategory(categoryId);
            log.info("카테고리별 상품 조회 성공: {} 개", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("카테고리별 상품 조회 실패: categoryId={}", categoryId, e);
            throw e;
        }
    }

    /**
     * 모든 판매중인 상품 조회 (페이징)
     */
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getAllActiveProducts(Pageable pageable) {
        try {
            log.info("전체 상품 조회 요청: page={}, size={}, sort={}",
                    pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
            Page<ProductResponse> products = customerProductService.getAllActiveProducts(pageable);
            log.info("전체 상품 조회 성공: totalElements={}, totalPages={}",
                    products.getTotalElements(), products.getTotalPages());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("전체 상품 조회 실패: pageable={}", pageable, e);
            throw e;
        }
    }

    /**
     * 상품 상세 조회 (판매중인 상품만)
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getActiveProduct(@PathVariable Long id) {
        try {
            log.info("상품 상세 조회 요청: id={}", id);
            ProductResponse product = customerProductService.getActiveProduct(id);
            log.info("상품 상세 조회 성공: productName={}", product.name());
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("상품 상세 조회 실패: id={}", id, e);
            throw e;
        }
    }

    /**
     * 상품명으로 검색 (판매중인 상품만)
     */
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponse>> searchActiveProducts(
            @RequestParam String keyword,
            Pageable pageable) {
        try {
            log.info("상품 검색 요청: keyword={}", keyword);
            Page<ProductResponse> products = customerProductService.searchActiveProducts(keyword, pageable);
            log.info("상품 검색 성공: {} 개 발견", products.getTotalElements());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("상품 검색 실패: keyword={}", keyword, e);
            throw e;
        }
    }

    /**
     * 가격 범위로 상품 조회 (판매중인 상품만)
     */
    @GetMapping("/products/price-range")
    public ResponseEntity<Page<ProductResponse>> getProductsByPriceRange(
            @RequestParam Long minPrice,
            @RequestParam Long maxPrice,
            Pageable pageable) {
        try {
            log.info("가격 범위 조회 요청: minPrice={}, maxPrice={}", minPrice, maxPrice);
            Page<ProductResponse> products = customerProductService.getActiveProductsByPriceRange(minPrice, maxPrice, pageable);
            log.info("가격 범위 조회 성공: {} 개 발견", products.getTotalElements());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("가격 범위 조회 실패: minPrice={}, maxPrice={}", minPrice, maxPrice, e);
            throw e;
        }
    }

    /**
     * 카테고리별 + 검색어 조합 조회
     */
    @GetMapping("/categories/{categoryId}/products/search")
    public ResponseEntity<Page<ProductResponse>> searchProductsInCategory(
            @PathVariable Long categoryId,
            @RequestParam String keyword,
            Pageable pageable) {
        try {
            log.info("카테고리 내 상품 검색 요청: categoryId={}, keyword={}", categoryId, keyword);
            Page<ProductResponse> products = customerProductService.searchActiveProductsInCategory(categoryId, keyword, pageable);
            log.info("카테고리 내 상품 검색 성공: {} 개 발견", products.getTotalElements());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("카테고리 내 상품 검색 실패: categoryId={}, keyword={}", categoryId, keyword, e);
            throw e;
        }
    }
}
