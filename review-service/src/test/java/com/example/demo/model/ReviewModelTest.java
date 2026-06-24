package com.example.demo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReviewModelTest {

    @Test
    void testGetterSetter() {

        Review r = new Review();

        r.setReviewText("Test");
        r.setRating(2);

        assertEquals("Test", r.getReviewText());
        assertEquals(2, r.getRating());
    }
}