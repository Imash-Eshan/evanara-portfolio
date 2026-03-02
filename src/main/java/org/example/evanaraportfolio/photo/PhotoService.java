package org.example.evanaraportfolio.photo;

import org.example.evanaraportfolio.category.Category;
import org.example.evanaraportfolio.category.CategoryRepository;
import org.example.evanaraportfolio.photo.dto.PhotoRequest;
import org.example.evanaraportfolio.photo.dto.PhotoResponse;
import org.example.evanaraportfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public PhotoResponse createPhoto(PhotoRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Photo photo = Photo.builder()
                .imageUrl(request.getImageUrl())
                .category(category)
                .caption(request.getCaption())
                .isFeatured(request.isFeatured())
                .build();

        Photo savedPhoto = photoRepository.save(photo);
        return mapToResponse(savedPhoto);
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> getAllPhotos() {
        return photoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PhotoResponse getPhotoById(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + id));
        return mapToResponse(photo);
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> getPhotosByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        return photoRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> getFeaturedPhotos() {
        return photoRepository.findByIsFeaturedTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PhotoResponse updatePhoto(Long id, PhotoRequest request) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        photo.setImageUrl(request.getImageUrl());
        photo.setCategory(category);
        photo.setCaption(request.getCaption());
        photo.setFeatured(request.isFeatured());

        Photo updatedPhoto = photoRepository.save(photo);
        return mapToResponse(updatedPhoto);
    }

    @Transactional
    public void deletePhoto(Long id) {
        if (!photoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Photo not found with id: " + id);
        }
        photoRepository.deleteById(id);
    }

    private PhotoResponse mapToResponse(Photo photo) {
        return PhotoResponse.builder()
                .id(photo.getId())
                .imageUrl(photo.getImageUrl())
                .category(mapCategoryToResponse(photo.getCategory()))
                .caption(photo.getCaption())
                .isFeatured(photo.isFeatured())
                .createdAt(photo.getCreatedAt())
                .build();
    }

    private org.example.evanaraportfolio.category.dto.CategoryResponse mapCategoryToResponse(Category category) {
        return org.example.evanaraportfolio.category.dto.CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .coverImageUrl(category.getCoverImageUrl())
                .createdAt(category.getCreatedAt())
                .build();
    }
}