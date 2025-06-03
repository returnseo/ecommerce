package com.study.ecommerce.domain.product.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductCreateRequest(
        @NotBlank(message = "상품명은 필수입니다.")
        String name,

        String description,

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 양수여야 합니다.")
        Long price,

        @NotNull(message = "재고 수량은 필수입니다.")
        @Positive(message = "재고 수량은 양수여야 합니다.")
        Integer stockQuantity,

        @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId

) {
}
