package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService service;

    @Test
    void testGetAllReviews() throws Exception {

        Review review = new Review();
        review.setReviewText("Good");
        review.setRating(5);

        when(service.getAll()).thenReturn(List.of(review));

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewText").value("Good"));
    }

    @Test
    void testAddReview() throws Exception {

        Review review = new Review();
        review.setReviewText("Nice");
        review.setRating(4);

        when(service.save(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/reviews")
                .contentType("application/json")
                .content("""
                    {
                        "reviewText": "Nice",
                        "rating": 4
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewText").value("Nice"));
    }
}