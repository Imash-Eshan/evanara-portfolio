package org.example.evanaraportfolio.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Slug is required")
    @Size(max = 100, message = "Slug must be less than 100 characters")
    private String slug;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private String coverImageUrl;
}