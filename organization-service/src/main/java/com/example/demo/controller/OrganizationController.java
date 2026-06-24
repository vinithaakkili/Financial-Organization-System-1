package com.example.demo.controller;

import com.example.demo.entity.Organization;
import com.example.demo.service.OrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@Tag(name = "Organization API", description = "Operations related to organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService service;

    @Operation(summary = "Get all organizations", description = "Fetch all organization records")
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred");
        }
    }

    @Operation(summary = "Add organization", description = "Create a new organization")
    @PostMapping
    public ResponseEntity<Organization> addOrganization(@RequestBody Organization org) {
        return ResponseEntity.ok(service.save(org));
    }
}