package org.example.evanaraportfolio.promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByIsActiveTrue();

    @Query("SELECT p FROM Promotion p WHERE p.startDate <= :currentDate AND p.endDate >= :currentDate AND p.isActive = true")
    List<Promotion> findActivePromotions(@Param("currentDate") LocalDate currentDate);

    List<Promotion> findByEndDateBefore(LocalDate date);
}