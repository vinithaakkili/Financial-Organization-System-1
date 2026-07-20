package com.example.demo.controller;

import com.example.demo.dto.RatingRequest;
import com.example.demo.model.Rating;
import com.example.demo.model.RatingStatus;
import com.example.demo.service.RatingService;

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

@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc(addFilters = false)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService service;

    @Test
    void testAddRating() throws Exception {

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setOrganizationId(6L);
        rating.setScore(5.0);
        rating.setRatingCategory("Financial Stability");
        rating.setRemarks("Strong financial performance");
        rating.setStatus(RatingStatus.PENDING);

        when(service.addRating(any(RatingRequest.class)))
        .thenReturn(rating);

        mockMvc.perform(
                post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "organizationId": 6,
                              "score": 5.0,
                              "ratingCategory": "Financial Stability",
                              "remarks": "Strong financial performance"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.organizationId").value(6))
                .andExpect(jsonPath("$.score").value(5.0))
                .andExpect(
                        jsonPath("$.ratingCategory")
                                .value("Financial Stability"))
                .andExpect(
                        jsonPath("$.status")
                                .value("PENDING"));
    }

    @Test
    void testGetAllRatingsEmpty() throws Exception {

        when(service.getAllRatings())
                .thenReturn(List.of());

        mockMvc.perform(get("/ratings"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetAllRatingsMultiple() throws Exception {

        Rating first = new Rating();
        first.setId(1L);
        first.setOrganizationId(6L);
        first.setScore(5.0);
        first.setStatus(RatingStatus.APPROVED);

        Rating second = new Rating();
        second.setId(2L);
        second.setOrganizationId(7L);
        second.setScore(3.0);
        second.setStatus(RatingStatus.PENDING);

        when(service.getAllRatings())
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5.0))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("APPROVED"))
                .andExpect(jsonPath("$[1].score").value(3.0))
                .andExpect(
                        jsonPath("$[1].status")
                                .value("PENDING"));
    }

    @Test
    void testGetPendingRatings() throws Exception {

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setOrganizationId(6L);
        rating.setScore(4.5);
        rating.setStatus(RatingStatus.PENDING);

        when(service.getPendingRatings())
                .thenReturn(List.of(rating));

        mockMvc.perform(get("/ratings/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("PENDING"));
    }

    @Test
    void testGetApprovedRatings() throws Exception {

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setOrganizationId(6L);
        rating.setScore(4.5);
        rating.setStatus(RatingStatus.APPROVED);

        when(service.getApprovedRatings())
                .thenReturn(List.of(rating));

        mockMvc.perform(get("/ratings/approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(4.5))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("APPROVED"));
    }

    @Test
    void testGetRatingsByOrganization() throws Exception {

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setOrganizationId(6L);
        rating.setScore(4.5);
        rating.setStatus(RatingStatus.APPROVED);

        when(service.getApprovedRatingsByOrganization(6L))
                .thenReturn(List.of(rating));

        mockMvc.perform(
                get("/ratings/organization/6"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].organizationId")
                                .value(6))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("APPROVED"));
    }

    @Test
    void testApproveRating() throws Exception {

        Rating approvedRating = new Rating();
        approvedRating.setId(1L);
        approvedRating.setOrganizationId(6L);
        approvedRating.setScore(4.5);
        approvedRating.setStatus(
                RatingStatus.APPROVED);

        when(service.approveRating(1L))
                .thenReturn(approvedRating);

        mockMvc.perform(
                put("/ratings/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(
                        jsonPath("$.status")
                                .value("APPROVED"));
    }

    @Test
    void testRejectRating() throws Exception {

        Rating rejectedRating = new Rating();
        rejectedRating.setId(1L);
        rejectedRating.setOrganizationId(6L);
        rejectedRating.setScore(2.0);
        rejectedRating.setStatus(
                RatingStatus.REJECTED);

        when(service.rejectRating(1L))
                .thenReturn(rejectedRating);

        mockMvc.perform(
                put("/ratings/1/reject"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("REJECTED"));
    }

    @Test
    void testAddRatingInvalidScore() throws Exception {

    	when(service.addRating(any(RatingRequest.class)))
                .thenThrow(
                        new IllegalArgumentException(
                                "Score must be between 1 and 5"));

        mockMvc.perform(
                post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "organizationId": 6,
                              "score": 10
                            }
                            """))
                .andExpect(status().isBadRequest());
    }
}