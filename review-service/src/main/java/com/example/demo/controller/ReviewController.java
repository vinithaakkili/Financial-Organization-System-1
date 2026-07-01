package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return service.save(review);
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return service.getAll();   // ✅ FIXED
    }
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return service.getById(id);
    }
}