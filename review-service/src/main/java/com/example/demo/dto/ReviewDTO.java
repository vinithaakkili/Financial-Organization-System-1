package com.example.demo.dto;

public class ReviewDTO {

    private Long id;
    private String organizationName;
    private String comment;

    public Long getId() {
        return id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getComment() {
        return comment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}