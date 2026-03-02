package org.example.evanaraportfolio.promotion;

import org.example.evanaraportfolio.promotion.dto.PromotionRequest;
import org.example.evanaraportfolio.promotion.dto.PromotionResponse;
import org.example.evanaraportfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    @Transactional
    public PromotionResponse createPromotion(PromotionRequest request) {
        Promotion promotion = Promotion.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .bannerImageUrl(request.getBannerImageUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(request.isActive())
                .build();

        Promotion savedPromotion = promotionRepository.save(promotion);
        return mapToResponse(savedPromotion);
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PromotionResponse getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));
        return mapToResponse(promotion);
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> getActivePromotions() {
        LocalDate currentDate = LocalDate.now();
        return promotionRepository.findActivePromotions(currentDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PromotionResponse updatePromotion(Long id, PromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));

        promotion.setTitle(request.getTitle());
        promotion.setDescription(request.getDescription());
        promotion.setBannerImageUrl(request.getBannerImageUrl());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setActive(request.isActive());

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return mapToResponse(updatedPromotion);
    }

    @Transactional
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promotion not found with id: " + id);
        }
        promotionRepository.deleteById(id);
    }

    @Transactional
    public void deactivateExpiredPromotions() {
        LocalDate currentDate = LocalDate.now();
        List<Promotion> expiredPromotions = promotionRepository.findByEndDateBefore(currentDate);
        expiredPromotions.forEach(promotion -> promotion.setActive(false));
        promotionRepository.saveAll(expiredPromotions);
    }

    private PromotionResponse mapToResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .id(promotion.getId())
                .title(promotion.getTitle())
                .description(promotion.getDescription())
                .bannerImageUrl(promotion.getBannerImageUrl())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .isActive(promotion.isActive())
                .createdAt(promotion.getCreatedAt())
                .build();
    }
}