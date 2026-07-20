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

import com.example.demo.dto.RatingRequest;
import com.example.demo.model.Rating;
import com.example.demo.service.RatingService;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService service;

    public RatingController(RatingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(
            @RequestBody RatingRequest ratingRequest) {

        Rating savedRating = service.addRating(ratingRequest);

        return new ResponseEntity<>(
                savedRating,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        return ResponseEntity.ok(service.getAllRatings());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Rating>> getPendingRatings() {
        return ResponseEntity.ok(service.getPendingRatings());
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Rating>> getApprovedRatings() {
        return ResponseEntity.ok(service.getApprovedRatings());
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Rating>> getRatingsByOrganization(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                service.getApprovedRatingsByOrganization(organizationId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Rating> approveRating(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.approveRating(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Rating> rejectRating(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.rejectRating(id));
    }
}