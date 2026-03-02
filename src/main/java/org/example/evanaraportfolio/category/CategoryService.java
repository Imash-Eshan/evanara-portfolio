package org.example.evanaraportfolio.category;

import org.example.evanaraportfolio.category.dto.CategoryRequest;
import org.example.evanaraportfolio.category.dto.CategoryResponse;
import org.example.evanaraportfolio.exception.ResourceNotFoundException;
import org.example.evanaraportfolio.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // Check if name or slug already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Category with slug '" + request.getSlug() + "' already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .coverImageUrl(request.getCoverImageUrl())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToResponse(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return mapToResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Check if new name conflicts with existing category
        if (!category.getName().equals(request.getName()) &&
                categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        // Check if new slug conflicts with existing category
        if (!category.getSlug().equals(request.getSlug()) &&
                categoryRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Category with slug '" + request.getSlug() + "' already exists");
        }

        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setCoverImageUrl(request.getCoverImageUrl());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .coverImageUrl(category.getCoverImageUrl())
                .createdAt(category.getCreatedAt())
                .build();
    }
}