package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "organization_name",
        nullable = false
    )
    private String organizationName;

    @Column(
        name = "registration_number",
        nullable = false,
        unique = true
    )
    private String registrationNumber;

    private String organizationType;
    private String email;
    private String phone;
    private String address;
    private String representativeName;
    private String representativeEmail;

    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    private String rejectionReason;
}