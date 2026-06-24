package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RatingModelTest {

    @Test
    void testGettersAndSetters() {

        Rating rating = new Rating();

        rating.setId(1L);
        rating.setOrganizationName("HDFC Bank");
        rating.setScore(5.0);

        assertEquals(1L, rating.getId());
        assertEquals("HDFC Bank", rating.getOrganizationName());
        assertEquals(5.0, rating.getScore());
    }
}