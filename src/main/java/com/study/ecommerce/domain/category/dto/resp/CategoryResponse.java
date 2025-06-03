package com.study.ecommerce.domain.category.dto.resp;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        Integer depth,
        Long parentId,
        List<CategoryResponse> children
) {
}
