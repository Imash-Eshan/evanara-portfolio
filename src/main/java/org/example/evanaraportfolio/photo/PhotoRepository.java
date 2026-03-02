package org.example.evanaraportfolio.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByCategoryId(Long categoryId);
    List<Photo> findByIsFeaturedTrue();
    List<Photo> findByCategoryIdAndIsFeaturedTrue(Long categoryId);
}