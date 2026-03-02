package org.example.evanaraportfolio.photo;

import org.example.evanaraportfolio.photo.dto.PhotoRequest;
import org.example.evanaraportfolio.photo.dto.PhotoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping
    public ResponseEntity<List<PhotoResponse>> getAllPhotos() {
        return ResponseEntity.ok(photoService.getAllPhotos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponse> getPhotoById(@PathVariable Long id) {
        return ResponseEntity.ok(photoService.getPhotoById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PhotoResponse>> getPhotosByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(photoService.getPhotosByCategory(categoryId));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<PhotoResponse>> getFeaturedPhotos() {
        return ResponseEntity.ok(photoService.getFeaturedPhotos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PhotoResponse> createPhoto(@Valid @RequestBody PhotoRequest request) {
        return new ResponseEntity<>(photoService.createPhoto(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PhotoResponse> updatePhoto(
            @PathVariable Long id,
            @Valid @RequestBody PhotoRequest request) {
        return ResponseEntity.ok(photoService.updatePhoto(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
}