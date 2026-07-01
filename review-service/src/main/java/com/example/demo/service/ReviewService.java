package com.example.demo.service;

import com.example.demo.exception.ReviewNotFoundException;
import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    public Review save(Review review) {
        return repository.save(review);
    }

    public List<Review> getAll() {
        return repository.findAll();
    }
    public Review getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review not found with id: " + id));
    }
}