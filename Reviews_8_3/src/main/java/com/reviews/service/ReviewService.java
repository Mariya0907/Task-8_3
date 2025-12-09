package com.reviews.service;

import com.reviews.domain.Review;
import com.reviews.repo.ReviewRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review getById(@NotNull Long id) {
        return reviewRepository.findById(id).orElseThrow();
    }

    public Review create(@Valid Review review) {
        review.setId(null);
        return reviewRepository.save(review);
    }

    public Review update(@NotNull Long id, @Valid Review updated) {
        Review existing = getById(id);
        existing.setUserName(updated.getUserName());
        existing.setProduct(updated.getProduct());
        existing.setRating(updated.getRating());
        existing.setComment(updated.getComment());
        return reviewRepository.save(existing);
    }

    public void delete(@NotNull Long id) {
        reviewRepository.deleteById(id);
    }
}





