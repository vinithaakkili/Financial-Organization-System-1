package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ReviewNotFoundException;
import com.example.demo.model.Review;
import com.example.demo.model.ReviewStatus;
import com.example.demo.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public Review addReview(Review review) {

        if (review.getOrganizationId() == null) {
            throw new IllegalArgumentException(
                    "Organization ID is required");
        }

        if (review.getStars() == null
                || review.getStars() < 1
                || review.getStars() > 5) {

            throw new IllegalArgumentException(
                    "Stars must be between 1 and 5");
        }

        if (review.getComment() == null
                || review.getComment().isBlank()) {

            throw new IllegalArgumentException(
                    "Review comment is required");
        }

        review.setStatus(ReviewStatus.PENDING);

        return repository.save(review);
    }

    public List<Review> getAllReviews() {
        return repository.findAll();
    }

    public List<Review> getPendingReviews() {
        return repository.findByStatus(
                ReviewStatus.PENDING);
    }

    public List<Review> getApprovedReviews() {
        return repository.findByStatus(
                ReviewStatus.APPROVED);
    }

    public List<Review> getApprovedReviewsByOrganization(
            Long organizationId) {

        return repository.findByOrganizationIdAndStatus(
                organizationId,
                ReviewStatus.APPROVED);
    }

    public Review getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review not found with id: " + id));
    }

    public Review approveReview(Long id) {

        Review review = getById(id);

        review.setStatus(ReviewStatus.APPROVED);

        return repository.save(review);
    }

    public Review rejectReview(Long id) {

        Review review = getById(id);

        review.setStatus(ReviewStatus.REJECTED);

        return repository.save(review);
    }
}