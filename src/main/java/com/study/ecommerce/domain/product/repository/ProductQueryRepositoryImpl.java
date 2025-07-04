package com.study.ecommerce.domain.product.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ecommerce.domain.category.entity.QCategory;
import com.study.ecommerce.domain.product.dto.req.ProductSearchCondition;
import com.study.ecommerce.domain.product.dto.resp.ProductSummaryDto;
import com.study.ecommerce.domain.product.entity.Product.ProductStatus;
import com.study.ecommerce.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductSummaryDto> searchProducts(
            ProductSearchCondition condition,
            Pageable pageable
    ) {

        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        List<ProductSummaryDto> content = queryFactory
                .select(Projections.constructor(ProductSummaryDto.class,
                        product.id,
                        product.name,
                        product.price,
                        product.stockQuantity,
                        category.name.coalesce("분류 없음").as("categoryName"),
                        product.status))
                .from(product)
                .leftJoin(category).on(product.categoryId.eq(category.id))
                .where(
                        keywordContains(condition.keyword()),
                        categoryIdEq(condition.categoryId()),
                        priceGoe(BigDecimal.valueOf(condition.minPrice())),
                        priceLoe(BigDecimal.valueOf(condition.maxPrice())),
                        sellerIdEq(condition.sellerId()),
                        statusActive()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, product))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        keywordContains(condition.keyword()),
                        categoryIdEq(condition.categoryId()),
                        priceGoe(BigDecimal.valueOf(condition.minPrice())),
                        priceLoe(BigDecimal.valueOf(condition.maxPrice())),
                        sellerIdEq(condition.sellerId()),
                        statusActive()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    /**
     * 키워드 검색 조건 (상품명 또는 설명에 포함)
     * @param keyword
     * @return BooleanExpression
     */
    private BooleanExpression keywordContains(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        QProduct product = QProduct.product;
        return product.name.containsIgnoreCase(keyword)
                .or(product.description.containsIgnoreCase(keyword));
    }

    /**
     *  카테고리 ID 조건
     * @param categoryId
     * @return BooleanExpression
     */
    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? QProduct.product.categoryId.eq(categoryId) : null;
    }

    private BooleanExpression priceGoe(BigDecimal minPrice) {
        return minPrice != null ? QProduct.product.price.goe(minPrice) : null;
    }

    private BooleanExpression priceLoe(BigDecimal maxPrice) {
        return maxPrice != null ? QProduct.product.price.loe(maxPrice) : null;
    }

    private BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? QProduct.product.sellerId.eq(sellerId) : null;
    }

    private BooleanExpression statusActive() {
        return QProduct.product.status.eq(ProductStatus.ACTIVE);
    }

    private BooleanExpression priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return priceGoe(minPrice).and(priceLoe(maxPrice));
    }

    private BooleanExpression stockAvailable() {
        return QProduct.product.stockQuantity.gt(0);
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable, QProduct product) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                switch (order.getProperty()) {
                    case "price" :
                        return order.isAscending() ? product.price.asc() : product.price.desc();

                    case "createdAt":
                        return order.isAscending() ? product.createdAt.asc() : product.createdAt.desc();

                    default:
                        return product.id.desc();
                }
            }
        }

        return product.id.desc();
    }

}
