package com.example.demo.dto;

public class RatingDTO {

    private Long id;
    private String organizationName;
    private int score;

    public Long getId() {
        return id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public int getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setScore(int score) {
        this.score = score;
    }
}