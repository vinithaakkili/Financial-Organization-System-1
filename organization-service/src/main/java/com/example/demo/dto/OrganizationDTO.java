package com.example.demo.dto;

public class OrganizationDTO {

    private Long id;
    private String organizationName;

    public OrganizationDTO() {}

    public OrganizationDTO(Long id, String organizationName) {
        this.id = id;
        this.organizationName = organizationName;
    }

    public Long getId() {
        return id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}