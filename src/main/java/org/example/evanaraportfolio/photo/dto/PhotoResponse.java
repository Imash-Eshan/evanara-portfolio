package org.example.evanaraportfolio.photo.dto;

import org.example.evanaraportfolio.category.dto.CategoryResponse;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PhotoResponse {
    private Long id;
    private String imageUrl;
    private CategoryResponse category;
    private String caption;
    private boolean isFeatured;
    private LocalDateTime createdAt;
}