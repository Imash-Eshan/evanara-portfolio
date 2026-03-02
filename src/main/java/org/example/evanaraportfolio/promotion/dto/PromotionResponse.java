package org.example.evanaraportfolio.promotion.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@Builder
public class PromotionResponse {
    private Long id;
    private String title;
    private String description;
    private String bannerImageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private LocalDateTime createdAt;
}