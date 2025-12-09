package com.reviewsfx.service;

import com.reviewsfx.config.RestTemplateConfig;
import com.reviewsfx.model.Review;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ReviewApiService {
    private static final String BASE_URL = "http://localhost:8080/api/reviews";
    private final RestTemplate restTemplate;

    public ReviewApiService() {
        this.restTemplate = RestTemplateConfig.restTemplate();
    }

    public List<Review> getAllReviews() {
        try {
            ResponseEntity<List<Review>> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Review>>() {}
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching reviews: " + e.getMessage(), e);
        }
    }

    public Review getReviewById(Long id) {
        try {
            ResponseEntity<Review> response = restTemplate.getForEntity(
                    BASE_URL + "/" + id,
                    Review.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching review by id: " + e.getMessage(), e);
        }
    }

    public Review createReview(Review review) {
        try {
            ResponseEntity<Review> response = restTemplate.postForEntity(
                    BASE_URL,
                    review,
                    Review.class
            );
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to create review: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error creating review: " + e.getMessage(), e);
        }
    }

    public Review updateReview(Long id, Review review) {
        try {
            ResponseEntity<Review> response = restTemplate.exchange(
                    BASE_URL + "/" + id,
                    HttpMethod.PUT,
                    new org.springframework.http.HttpEntity<>(review),
                    Review.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Error updating review: " + e.getMessage(), e);
        }
    }

    public void deleteReview(Long id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
        } catch (RestClientException e) {
            throw new RuntimeException("Error deleting review: " + e.getMessage(), e);
        }
    }
}

