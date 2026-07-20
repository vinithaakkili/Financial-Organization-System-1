package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.RatingRequest;
import com.example.demo.model.Rating;
import com.example.demo.model.RatingStatus;
import com.example.demo.repository.RatingRepository;

@Service
public class RatingService {

	private final RatingRepository repository;

	public RatingService(RatingRepository repository) {
	    this.repository = repository;
	}

	public Rating addRating(RatingRequest ratingRequest) {

	    if (ratingRequest.getOrganizationId() == null) {
	        throw new IllegalArgumentException(
	                "Organization ID is required");
	    }

	    if (ratingRequest.getScore() == null
	            || ratingRequest.getScore() < 1
	            || ratingRequest.getScore() > 5) {

	        throw new IllegalArgumentException(
	                "Score must be between 1 and 5");
	    }

	    Rating rating = new Rating();
	    rating.setOrganizationId(ratingRequest.getOrganizationId());
	    rating.setScore(ratingRequest.getScore());
	    rating.setRatingCategory(ratingRequest.getRatingCategory());
	    rating.setRemarks(ratingRequest.getRemarks());

	    rating.setStatus(
	            ratingRequest.getStatus() == null
	                    ? RatingStatus.PENDING
	                    : ratingRequest.getStatus());

	    return repository.save(rating);
	}

    public List<Rating> getAllRatings() {
        return repository.findAll();
    }

    public List<Rating> getPendingRatings() {
        return repository.findByStatus(
                RatingStatus.PENDING);
    }

    public List<Rating> getApprovedRatings() {
        return repository.findByStatus(
                RatingStatus.APPROVED);
    }

    public List<Rating> getApprovedRatingsByOrganization(
            Long organizationId) {

        return repository.findByOrganizationIdAndStatus(
                organizationId,
                RatingStatus.APPROVED);
    }

    public Rating approveRating(Long id) {

        Rating rating = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rating not found"));

        rating.setStatus(RatingStatus.APPROVED);

        return repository.save(rating);
    }

    public Rating rejectRating(Long id) {

        Rating rating = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rating not found"));

        rating.setStatus(RatingStatus.REJECTED);

        return repository.save(rating);
    }
}