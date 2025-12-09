package com.reviews.repo;

import com.reviews.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Поиск отзывов по продукту
    List<Review> findByProductContainingIgnoreCase(String product);

    // Поиск отзывов по имени пользователя
    List<Review> findByUserNameContainingIgnoreCase(String userName);

    // Поиск отзывов по рейтингу
    List<Review> findByRating(Integer rating);

    // Поиск отзывов с рейтингом больше или равным указанному
    List<Review> findByRatingGreaterThanEqual(Integer rating);
}

