package org.example.evanaraportfolio.promotion;

import org.example.evanaraportfolio.promotion.dto.PromotionRequest;
import org.example.evanaraportfolio.promotion.dto.PromotionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> getPromotionById(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PromotionResponse>> getActivePromotions() {
        return ResponseEntity.ok(promotionService.getActivePromotions());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionResponse> createPromotion(@Valid @RequestBody PromotionRequest request) {
        return new ResponseEntity<>(promotionService.createPromotion(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionResponse> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequest request) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deactivate-expired")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateExpiredPromotions() {
        promotionService.deactivateExpiredPromotions();
        return ResponseEntity.ok().build();
    }
}