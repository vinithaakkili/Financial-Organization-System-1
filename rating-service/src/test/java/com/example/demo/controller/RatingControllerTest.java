package com.example.demo.controller;

import com.example.demo.model.Rating;
import com.example.demo.service.RatingService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc(addFilters = false)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService service;

    @Test
    void testAddRating() throws Exception {

        Rating r = new Rating();
        r.setScore(5);

        when(service.save(any(Rating.class))).thenReturn(r);

        mockMvc.perform(post("/ratings")
                .contentType("application/json")
                .content("{\"score\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(5));
    }
    @Test
    void testGetAllRatings_Empty() throws Exception {

        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/ratings"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    void testGetAllRatings_Multiple() throws Exception {

        Rating r1 = new Rating();
        r1.setScore(5);

        Rating r2 = new Rating();
        r2.setScore(3);

        when(service.getAll()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5))
                .andExpect(jsonPath("$[1].score").value(3));
    }
    @Test
    void testAddRating_InvalidJson() throws Exception {

        mockMvc.perform(post("/ratings")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isOk()); // adjust if validation added
    }
    

}