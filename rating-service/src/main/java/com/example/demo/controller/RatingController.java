package com.example.demo.controller;

import com.example.demo.model.Rating;
import com.example.demo.service.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService service;

    @PostMapping
    public Rating addRating(@RequestBody Rating rating) {
        return service.save(rating);
    }

    @GetMapping
    public List<Rating> getAllRatings() {
        return service.getAll();
    }
    @GetMapping("/async")
    public CompletableFuture<List<Rating>> getAllAsync() {
        return service.getAllAsync();
    }
}
