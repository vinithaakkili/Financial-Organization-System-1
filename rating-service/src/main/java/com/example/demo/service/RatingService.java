package com.example.demo.service;

import com.example.demo.model.Rating;
import com.example.demo.repository.RatingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RatingService {

    private static final Logger log = LoggerFactory.getLogger(RatingService.class);

    @Autowired
    private RatingRepository repository;

    public Rating save(Rating rating) {
        return repository.save(rating);
    }

    public List<Rating> getAll() {
        return repository.findAll();
    }

    @Async
    public CompletableFuture<List<Rating>> getAllAsync() {

        log.info("Fetching ratings asynchronously...");

        List<Rating> list = repository.findAll();

        return CompletableFuture.completedFuture(list);
    }
}