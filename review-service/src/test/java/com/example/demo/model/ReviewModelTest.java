package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ReviewModelTest {

    @Test
    void testGettersAndSetters() {

        Review review = new Review();

        review.setId(1L);
        review.setOrganizationId(8L);
        review.setUserId(1L);
        review.setStars(5);
        review.setComment("Excellent customer support");
        review.setStatus(ReviewStatus.PENDING);

        assertEquals(1L, review.getId());
        assertEquals(8L, review.getOrganizationId());
        assertEquals(1L, review.getUserId());
        assertEquals(5, review.getStars());

        assertEquals(
                "Excellent customer support",
                review.getComment());

        assertEquals(
                ReviewStatus.PENDING,
                review.getStatus());
    }
}