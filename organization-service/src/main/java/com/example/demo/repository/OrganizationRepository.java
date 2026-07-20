package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Organization;
import com.example.demo.entity.OrganizationStatus;

@Repository
public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {

    List<Organization> findByStatus(OrganizationStatus status);

    boolean existsByRegistrationNumber(String registrationNumber);
}