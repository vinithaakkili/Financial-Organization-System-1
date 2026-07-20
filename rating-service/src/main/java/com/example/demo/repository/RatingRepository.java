package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Rating;
import com.example.demo.model.RatingStatus;

public interface RatingRepository
        extends JpaRepository<Rating, Long> {

    List<Rating> findByStatus(RatingStatus status);

    List<Rating> findByOrganizationIdAndStatus(
            Long organizationId,
            RatingStatus status);
}