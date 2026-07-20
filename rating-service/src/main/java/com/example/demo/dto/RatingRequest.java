package com.example.demo.dto;

import com.example.demo.model.RatingStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingRequest {

    private Long organizationId;
    private Double score;
    private String ratingCategory;
    private String remarks;
    private RatingStatus status;
}