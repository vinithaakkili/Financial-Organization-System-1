package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Review> addReview(
            @RequestBody Review review) {

        Review savedReview =
                service.addReview(review);

        return new ResponseEntity<>(
                savedReview,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Review>>
            getAllReviews() {

        return ResponseEntity.ok(
                service.getAllReviews());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Review>>
            getPendingReviews() {

        return ResponseEntity.ok(
                service.getPendingReviews());
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Review>>
            getApprovedReviews() {

        return ResponseEntity.ok(
                service.getApprovedReviews());
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Review>>
            getApprovedReviewsByOrganization(
                    @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                service.getApprovedReviewsByOrganization(
                        organizationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getById(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Review> approveReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.approveReview(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Review> rejectReview(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.rejectReview(id));
    }
}