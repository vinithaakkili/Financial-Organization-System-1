package com.example.demo.service;

import com.example.demo.model.Rating;
import com.example.demo.repository.RatingRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository repository;

    @InjectMocks
    private RatingService service;

    @Test
    void testGetAllRatings() {

        Rating r1 = new Rating();
        r1.setScore(5);

        Rating r2 = new Rating();
        r2.setScore(4);

        when(repository.findAll()).thenReturn(List.of(r1, r2));

        List<Rating> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testSaveRating() {

        Rating rating = new Rating();
        rating.setScore(5);

        when(repository.save(rating)).thenReturn(rating);

        Rating saved = service.save(rating);

        assertEquals(5, saved.getScore());
        verify(repository).save(rating);
    }
}