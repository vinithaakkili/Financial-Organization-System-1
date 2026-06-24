package com.example.demo.service;

import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewService service;

    public ReviewServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReview() {

        Review r = new Review();
        r.setRating(5);

        when(repository.save(r)).thenReturn(r);

        Review result = service.save(r);

        assertEquals(5, result.getRating());
    }

    @Test
    void testGetAllReviews() {

        Review r = new Review();
        r.setRating(3);

        when(repository.findAll()).thenReturn(List.of(r));

        List<Review> list = service.getAll();

        assertEquals(1, list.size());
    }
}