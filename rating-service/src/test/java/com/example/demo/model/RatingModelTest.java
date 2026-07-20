package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RatingModelTest {

    @Test
    void testGettersAndSetters() {

        Rating rating = new Rating();

        rating.setId(1L);
        rating.setOrganizationId(6L);
        rating.setScore(5.0);
        rating.setRatingCategory(
                "Financial Stability");
        rating.setRemarks(
                "Strong financial performance");
        rating.setStatus(RatingStatus.PENDING);

        assertEquals(1L, rating.getId());
        assertEquals(
                6L,
                rating.getOrganizationId());
        assertEquals(
                5.0,
                rating.getScore());
        assertEquals(
                "Financial Stability",
                rating.getRatingCategory());
        assertEquals(
                "Strong financial performance",
                rating.getRemarks());
        assertEquals(
                RatingStatus.PENDING,
                rating.getStatus());
    }
}