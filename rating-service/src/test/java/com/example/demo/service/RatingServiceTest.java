package com.example.demo.service;

import com.example.demo.dto.RatingRequest;
import com.example.demo.model.Rating;
import com.example.demo.model.RatingStatus;
import com.example.demo.repository.RatingRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository repository;

    @InjectMocks
    private RatingService service;

    @Test
    void testGetAllRatings() {

        Rating first = new Rating();
        first.setScore(5.0);

        Rating second = new Rating();
        second.setScore(4.0);

        when(repository.findAll())
                .thenReturn(List.of(first, second));

        List<Rating> result = service.getAllRatings();

        assertEquals(2, result.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllRatingsEmpty() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<Rating> result = service.getAllRatings();

        assertEquals(0, result.size());
    }

    @Test
    void testAddRating() {

        RatingRequest request = new RatingRequest();
        request.setOrganizationId(6L);
        request.setScore(5.0);
        request.setRatingCategory("Financial Stability");
        request.setRemarks("Strong financial performance");

        when(repository.save(any(Rating.class)))
                .thenAnswer(invocation -> {

                    Rating saved = invocation.getArgument(0);
                    saved.setId(1L);

                    return saved;
                });

        Rating saved = service.addRating(request);

        assertEquals(1L, saved.getId());
        assertEquals(6L, saved.getOrganizationId());
        assertEquals(5.0, saved.getScore());
        assertEquals(
                "Financial Stability",
                saved.getRatingCategory());
        assertEquals(
                "Strong financial performance",
                saved.getRemarks());
        assertEquals(
                RatingStatus.PENDING,
                saved.getStatus());

        ArgumentCaptor<Rating> ratingCaptor =
                ArgumentCaptor.forClass(Rating.class);

        verify(repository).save(ratingCaptor.capture());

        Rating persistedRating = ratingCaptor.getValue();

        assertEquals(
                6L,
                persistedRating.getOrganizationId());
        assertEquals(
                5.0,
                persistedRating.getScore());
        assertEquals(
                RatingStatus.PENDING,
                persistedRating.getStatus());
    }

    @Test
    void testAddRatingWithoutOrganizationId() {

        RatingRequest request = new RatingRequest();
        request.setScore(4.0);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.addRating(request));

        assertEquals(
                "Organization ID is required",
                exception.getMessage());
    }

    @Test
    void testAddRatingWithScoreAboveFive() {

        RatingRequest request = new RatingRequest();
        request.setOrganizationId(6L);
        request.setScore(6.0);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.addRating(request));

        assertEquals(
                "Score must be between 1 and 5",
                exception.getMessage());
    }

    @Test
    void testAddRatingWithScoreBelowOne() {

        RatingRequest request = new RatingRequest();
        request.setOrganizationId(6L);
        request.setScore(0.0);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.addRating(request));

        assertEquals(
                "Score must be between 1 and 5",
                exception.getMessage());
    }

    @Test
    void testAddRatingWithNullScore() {

        RatingRequest request = new RatingRequest();
        request.setOrganizationId(6L);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.addRating(request));

        assertEquals(
                "Score must be between 1 and 5",
                exception.getMessage());
    }

    @Test
    void testAddRatingWithProvidedStatus() {

        RatingRequest request = new RatingRequest();
        request.setOrganizationId(6L);
        request.setScore(4.0);
        request.setStatus(RatingStatus.APPROVED);

        when(repository.save(any(Rating.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Rating result = service.addRating(request);

        assertEquals(
                RatingStatus.APPROVED,
                result.getStatus());
    }

    @Test
    void testGetPendingRatings() {

        Rating rating = new Rating();
        rating.setStatus(RatingStatus.PENDING);

        when(repository.findByStatus(RatingStatus.PENDING))
                .thenReturn(List.of(rating));

        List<Rating> result =
                service.getPendingRatings();

        assertEquals(1, result.size());
        assertEquals(
                RatingStatus.PENDING,
                result.get(0).getStatus());
    }

    @Test
    void testGetApprovedRatings() {

        Rating rating = new Rating();
        rating.setStatus(RatingStatus.APPROVED);

        when(repository.findByStatus(RatingStatus.APPROVED))
                .thenReturn(List.of(rating));

        List<Rating> result =
                service.getApprovedRatings();

        assertEquals(1, result.size());
        assertEquals(
                RatingStatus.APPROVED,
                result.get(0).getStatus());
    }

    @Test
    void testGetApprovedRatingsByOrganization() {

        Rating rating = new Rating();
        rating.setOrganizationId(6L);
        rating.setStatus(RatingStatus.APPROVED);

        when(repository.findByOrganizationIdAndStatus(
                6L,
                RatingStatus.APPROVED))
                .thenReturn(List.of(rating));

        List<Rating> result =
                service.getApprovedRatingsByOrganization(6L);

        assertEquals(1, result.size());
        assertEquals(
                6L,
                result.get(0).getOrganizationId());
    }

    @Test
    void testApproveRating() {

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setOrganizationId(6L);
        rating.setScore(4.5);
        rating.setStatus(RatingStatus.PENDING);

        when(repository.findById(1L))
                .thenReturn(Optional.of(rating));

        when(repository.save(rating))
                .thenReturn(rating);

        Rating result =
                service.approveRating(1L);

        assertEquals(
                RatingStatus.APPROVED,
                result.getStatus());

        verify(repository).save(rating);
    }

    @Test
    void testRejectRating() {

        Rating rating = new Rating();
        rating.setId(2L);
        rating.setOrganizationId(6L);
        rating.setScore(2.0);
        rating.setStatus(RatingStatus.PENDING);

        when(repository.findById(2L))
                .thenReturn(Optional.of(rating));

        when(repository.save(rating))
                .thenReturn(rating);

        Rating result =
                service.rejectRating(2L);

        assertEquals(
                RatingStatus.REJECTED,
                result.getStatus());

        verify(repository).save(rating);
    }

    @Test
    void testApproveRatingNotFound() {

        when(repository.findById(10L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.approveRating(10L));

        assertEquals(
                "Rating not found",
                exception.getMessage());
    }

    @Test
    void testRejectRatingNotFound() {

        when(repository.findById(10L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.rejectRating(10L));

        assertEquals(
                "Rating not found",
                exception.getMessage());
    }
}