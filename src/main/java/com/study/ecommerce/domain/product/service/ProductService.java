package com.study.ecommerce.domain.product.service;

import com.study.ecommerce.domain.category.entity.Category;
import com.study.ecommerce.domain.category.repository.CategoryRepository;
import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.repository.MemberRepository;
import com.study.ecommerce.domain.product.dto.req.ProductCreateRequest;
import com.study.ecommerce.domain.product.dto.req.ProductUpdateRequest;
import com.study.ecommerce.domain.product.dto.resp.ProductResponse;
import com.study.ecommerce.domain.product.entity.Product;
import com.study.ecommerce.domain.product.repository.ProductRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request, String email) {
        Member seller = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("판매자를 찾을 수 없습니다."));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .status(Product.ProductStatus.ACTIVE)
                .sellerId(seller.getId())
                .categoryId(category.getId())
                .build();

        productRepository.save(product);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                category.getName()
        );
    }

    @Transactional
    public void deleteProduct(Long id, String email) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // 현재 사용자가 판매자인지 확인
        Member seller = memberRepository.findById(product.getSellerId())
                .orElseThrow(() -> new EntityNotFoundException("판매자를 찾을 수 없습니다."));

        if (!seller.getEmail().equals(email)) {
            throw new IllegalArgumentException("상품 삭제 권한이 없습니다.");
        }

        product.delete();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        // TODO
        return null;
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request, String email) {
        // 프로덕트를 찾고
        // 현재 사용자가 판매자인지 확인
        // 카테고리 찾고
        // 프로덕트 업데이트하고 -> jpa 더티체킹 더티캐싱?
        // 그리고 반환
        return null;
    }
}
