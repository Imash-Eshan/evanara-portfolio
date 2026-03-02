package org.example.evanaraportfolio.photo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PhotoRequest {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String caption;

    private boolean isFeatured;
}