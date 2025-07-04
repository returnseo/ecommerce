package com.study.ecommerce.domain.category.service;

import com.study.ecommerce.domain.category.dto.req.CategoryRequest;
import com.study.ecommerce.domain.category.dto.resp.CategoryResponse;
import com.study.ecommerce.domain.category.entity.Category;
import com.study.ecommerce.domain.category.repository.CategoryRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceCustom implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        // 모든 카테고리 조회
        List<Category> allCategories = categoryRepository.findAll();

        // 부모 Id 별로 하위 카테고리를 그룹화
        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(cat -> cat.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        // 루트 카테고리만 필터링
        List<Category> rootCategories = allCategories.stream()
                .filter(cat -> cat.getParentId() == null)
                .toList();


        return rootCategories.stream()
                .map(root -> buildCategoryHierarchy(root, childrenMap))
                .toList();
    }

    private CategoryResponse buildCategoryHierarchy(Category category, Map<Long, List<Category>> childrenMap) {
        List<CategoryResponse> children = new ArrayList<>();
        if (childrenMap.containsKey(category.getId())) {
            children = childrenMap.get(category.getId()).stream()
                    .map(child -> buildCategoryHierarchy(child, childrenMap))
                    .toList();
        }

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getParentId(),
                children
        );
    }

    @Override
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다"));

        List<Category> allCategories = categoryRepository.findAll();

        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(cat -> cat.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        return buildCategoryHierarchy(category, childrenMap);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Long parentId = null;
        int depth = 1;

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("상위 카테고리를 찾을 수 없습니다"));
            parentId = parent.getId();
            depth = parent.getDepth() + 1;
        }

        Category category = Category.builder()
                .name(request.name())
                .depth(depth)
                .parentId(parentId)
                .build();

        categoryRepository.save(category);

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getParentId(),
                List.of()
        );
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다"));

        Long parentId = null;
        int depth = 1;

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("상위 카테고리를 찾을 수 없습니다."));

            parentId = parent.getId();
            depth = parent.getDepth() + 1;

            // 자기 자신을 부모로 설정하는 경우가 없도록
            if (parent.getId().equals(category.getId())) {
                throw new IllegalArgumentException("자기 자신을 상위 카테고리로 설정할 수 없습니다.");
            }

            // 자신이 하위 카테고리를 부모로 설정하는 순환참조 금지
            if (isDescendant(category, parent)) {
                throw new IllegalArgumentException("하위 카테고리를 상위 카테고리로 설정할 수 없습니다.");
            }

        }

        category = Category.builder()
                .name(request.name())
                .depth(depth)
                .parentId(parentId)
                .build();

        categoryRepository.save(category);

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getParentId(),
                List.of()
        );
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        List<Category> children = categoryRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("하위 카테고리가 있는 카테고리는 삭제할 수 없습니다.");
        }

        Long productCount = categoryRepository.countProductsByCategory(id);
        if (productCount > 0) {
            throw new IllegalArgumentException("카테고리에 속한 상품이 있는 경우 삭제할 수 없습니다.");
        }

        categoryRepository.delete(category);
    }

    private boolean isDescendant(Category ancestor, Category descendant) {
        if (descendant.getParentId() == null) {
            return false;
        }

        if (descendant.getParentId().equals(ancestor.getId())) {
            return true;
        }

        // descendant 의 부모 카테고리를 조회
        Category parent = categoryRepository.findById(descendant.getParentId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다"));

        return isDescendant(ancestor, parent);
    }
}
