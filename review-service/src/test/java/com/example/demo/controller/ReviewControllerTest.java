package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.model.ReviewStatus;
import com.example.demo.service.ReviewService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        review.setId(1L);
        review.setOrganizationId(8L);
        review.setUserId(1L);
        review.setStars(5);
        review.setComment("Good customer service");
        review.setStatus(ReviewStatus.APPROVED);

        when(service.getAllReviews())
                .thenReturn(List.of(review));

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].organizationId").value(8))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].stars").value(5))
                .andExpect(
                        jsonPath("$[0].comment")
                                .value("Good customer service"))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("APPROVED"));
    }

    @Test
    void testGetAllReviewsEmpty() throws Exception {

        when(service.getAllReviews())
                .thenReturn(List.of());

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddReview() throws Exception {

        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setOrganizationId(8L);
        savedReview.setUserId(1L);
        savedReview.setStars(4);
        savedReview.setComment("Nice banking experience");
        savedReview.setStatus(ReviewStatus.PENDING);

        when(service.addReview(any(Review.class)))
                .thenReturn(savedReview);

        mockMvc.perform(
                        post("/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "organizationId": 8,
                                          "userId": 1,
                                          "stars": 4,
                                          "comment": "Nice banking experience"
                                        }
                                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.organizationId").value(8))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.stars").value(4))
                .andExpect(
                        jsonPath("$.comment")
                                .value("Nice banking experience"))
                .andExpect(
                        jsonPath("$.status")
                                .value("PENDING"));
    }

    @Test
    void testGetPendingReviews() throws Exception {

        Review review = new Review();
        review.setId(2L);
        review.setOrganizationId(8L);
        review.setStars(3);
        review.setComment("Average service");
        review.setStatus(ReviewStatus.PENDING);

        when(service.getPendingReviews())
                .thenReturn(List.of(review));

        mockMvc.perform(get("/reviews/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("PENDING"));
    }

    @Test
    void testGetApprovedReviews() throws Exception {

        Review review = new Review();
        review.setId(3L);
        review.setOrganizationId(8L);
        review.setStars(5);
        review.setComment("Excellent service");
        review.setStatus(ReviewStatus.APPROVED);

        when(service.getApprovedReviews())
                .thenReturn(List.of(review));

        mockMvc.perform(get("/reviews/approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stars").value(5))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("APPROVED"));
    }

    @Test
    void testGetApprovedReviewsByOrganization() throws Exception {

        Review review = new Review();
        review.setId(4L);
        review.setOrganizationId(8L);
        review.setStars(4);
        review.setComment("Reliable banking service");
        review.setStatus(ReviewStatus.APPROVED);

        when(service.getApprovedReviewsByOrganization(8L))
                .thenReturn(List.of(review));

        mockMvc.perform(get("/reviews/organization/8"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].organizationId")
                                .value(8))
                .andExpect(
                        jsonPath("$[0].comment")
                                .value("Reliable banking service"));
    }

    @Test
    void testApproveReview() throws Exception {

        Review approvedReview = new Review();
        approvedReview.setId(1L);
        approvedReview.setOrganizationId(8L);
        approvedReview.setStars(5);
        approvedReview.setComment("Excellent");
        approvedReview.setStatus(ReviewStatus.APPROVED);

        when(service.approveReview(1L))
                .thenReturn(approvedReview);

        mockMvc.perform(put("/reviews/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(
                        jsonPath("$.status")
                                .value("APPROVED"));
    }

    @Test
    void testRejectReview() throws Exception {

        Review rejectedReview = new Review();
        rejectedReview.setId(2L);
        rejectedReview.setOrganizationId(8L);
        rejectedReview.setStars(1);
        rejectedReview.setComment("Spam review");
        rejectedReview.setStatus(ReviewStatus.REJECTED);

        when(service.rejectReview(2L))
                .thenReturn(rejectedReview);

        mockMvc.perform(put("/reviews/2/reject"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("REJECTED"));
    }
}