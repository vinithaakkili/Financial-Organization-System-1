package com.example.demo.service;

import com.example.demo.model.Review;
import com.example.demo.model.ReviewStatus;
import com.example.demo.repository.ReviewRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewService service;

    @Test
    void testAddReview() {

        Review review = new Review();
        review.setOrganizationId(8L);
        review.setUserId(1L);
        review.setStars(5);
        review.setComment("Excellent banking service");

        when(repository.save(any(Review.class)))
                .thenAnswer(invocation -> {
                    Review savedReview = invocation.getArgument(0);
                    savedReview.setId(1L);
                    return savedReview;
                });

        Review result = service.addReview(review);

        assertEquals(1L, result.getId());
        assertEquals(8L, result.getOrganizationId());
        assertEquals(1L, result.getUserId());
        assertEquals(5, result.getStars());
        assertEquals(
                "Excellent banking service",
                result.getComment()
        );
        assertEquals(
                ReviewStatus.PENDING,
                result.getStatus()
        );

        verify(repository).save(review);
    }

    @Test
    void testAddReviewWithoutOrganizationId() {

        Review review = new Review();
        review.setUserId(1L);
        review.setStars(5);
        review.setComment("Good");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.addReview(review)
                );

        assertEquals(
                "Organization ID is required",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidReviewProvider")
    void testAddReviewWithInvalidData(
            Integer stars,
            String comment,
            String expectedMessage) {

        Review review = new Review();
        review.setOrganizationId(8L);
        review.setUserId(1L);
        review.setStars(stars);
        review.setComment(comment);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.addReview(review)
                );

        assertEquals(
                expectedMessage,
                exception.getMessage()
        );
    }

    static Stream<Arguments> invalidReviewProvider() {

        return Stream.of(
                Arguments.of(
                        6,
                        "Good",
                        "Stars must be between 1 and 5"
                ),
                Arguments.of(
                        0,
                        "Bad",
                        "Stars must be between 1 and 5"
                ),
                Arguments.of(
                        4,
                        "",
                        "Review comment is required"
                )
        );
    }

    @Test
    void testGetAllReviews() {

        Review review = new Review();
        review.setStars(3);

        when(repository.findAll())
                .thenReturn(List.of(review));

        List<Review> result = service.getAllReviews();

        assertEquals(1, result.size());

        verify(repository).findAll();
    }

    @Test
    void testGetAllReviewsEmpty() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<Review> result = service.getAllReviews();

        assertEquals(0, result.size());
    }

    @Test
    void testGetPendingReviews() {

        Review review = new Review();
        review.setStatus(ReviewStatus.PENDING);

        when(repository.findByStatus(ReviewStatus.PENDING))
                .thenReturn(List.of(review));

        List<Review> result =
                service.getPendingReviews();

        assertEquals(1, result.size());
        assertEquals(
                ReviewStatus.PENDING,
                result.get(0).getStatus()
        );
    }

    @Test
    void testGetApprovedReviews() {

        Review review = new Review();
        review.setStatus(ReviewStatus.APPROVED);

        when(repository.findByStatus(ReviewStatus.APPROVED))
                .thenReturn(List.of(review));

        List<Review> result =
                service.getApprovedReviews();

        assertEquals(1, result.size());
        assertEquals(
                ReviewStatus.APPROVED,
                result.get(0).getStatus()
        );
    }

    @Test
    void testGetApprovedReviewsByOrganization() {

        Review review = new Review();
        review.setOrganizationId(8L);
        review.setStatus(ReviewStatus.APPROVED);

        when(repository.findByOrganizationIdAndStatus(
                8L,
                ReviewStatus.APPROVED
        )).thenReturn(List.of(review));

        List<Review> result =
                service.getApprovedReviewsByOrganization(8L);

        assertEquals(1, result.size());
        assertEquals(
                8L,
                result.get(0).getOrganizationId()
        );
    }

    @Test
    void testApproveReview() {

        Review review = new Review();
        review.setId(1L);
        review.setStatus(ReviewStatus.PENDING);

        when(repository.findById(1L))
                .thenReturn(Optional.of(review));

        when(repository.save(review))
                .thenReturn(review);

        Review result = service.approveReview(1L);

        assertEquals(
                ReviewStatus.APPROVED,
                result.getStatus()
        );

        verify(repository).save(review);
    }

    @Test
    void testRejectReview() {

        Review review = new Review();
        review.setId(2L);
        review.setStatus(ReviewStatus.PENDING);

        when(repository.findById(2L))
                .thenReturn(Optional.of(review));

        when(repository.save(review))
                .thenReturn(review);

        Review result = service.rejectReview(2L);

        assertEquals(
                ReviewStatus.REJECTED,
                result.getStatus()
        );

        verify(repository).save(review);
    }

    @Test
    void testApproveReviewNotFound() {

        long reviewId = 99L;

        when(repository.findById(reviewId))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.approveReview(reviewId)
        );
    }
}