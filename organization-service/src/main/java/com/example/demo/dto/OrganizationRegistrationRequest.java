package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationRegistrationRequest {

    private String organizationName;
    private String registrationNumber;
    private String organizationType;
    private String email;
    private String phone;
    private String address;
    private String representativeName;
    private String representativeEmail;
    
}