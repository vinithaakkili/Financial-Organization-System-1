package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Review;
import com.example.demo.model.ReviewStatus;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByStatus(ReviewStatus status);

    List<Review> findByOrganizationId(
            Long organizationId);

    List<Review> findByOrganizationIdAndStatus(
            Long organizationId,
            ReviewStatus status);
}