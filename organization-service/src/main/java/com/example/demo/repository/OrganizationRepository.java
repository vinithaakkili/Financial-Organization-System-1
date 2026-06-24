package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}