package com.study.ecommerce.domain.product.repository;

import com.study.ecommerce.domain.product.dto.req.ProductSearchCondition;
import com.study.ecommerce.domain.product.dto.resp.ProductSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {
    Page<ProductSummaryDto> searchProducts(ProductSearchCondition condition, Pageable pageable);
}
