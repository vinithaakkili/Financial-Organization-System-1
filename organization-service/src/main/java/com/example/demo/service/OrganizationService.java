package com.example.demo.service;

import com.example.demo.entity.Organization;
import com.example.demo.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository repository;

    public List<Organization> getAll() {
        return repository.findAll();
    }
    public Organization save(Organization organization) {
        return repository.save(organization);
    }
}