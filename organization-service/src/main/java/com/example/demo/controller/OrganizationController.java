package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrganizationApprovalRequest;
import com.example.demo.dto.OrganizationRegistrationRequest;
import com.example.demo.entity.Organization;
import com.example.demo.service.OrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/organizations")
@Tag(
    name = "Organization API",
    description = "Organization registration and finance approval APIs"
)
public class OrganizationController {

	private final OrganizationService service;

	public OrganizationController(OrganizationService service) {
	    this.service = service;
	}

    @Operation(summary = "Register organization")
    @PostMapping("/register")
    public ResponseEntity<Organization> registerOrganization(
            @RequestBody OrganizationRegistrationRequest request) {

        Organization saved =
                service.registerOrganization(request);

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get every organization")
    @GetMapping
    public ResponseEntity<List<Organization>>
            getAllOrganizations() {

        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get pending organizations")
    @GetMapping("/pending")
    public ResponseEntity<List<Organization>>
            getPendingOrganizations() {

        return ResponseEntity.ok(
                service.getPendingOrganizations());
    }

    @Operation(summary = "Get approved organizations")
    @GetMapping("/approved")
    public ResponseEntity<List<Organization>>
            getApprovedOrganizations() {

        return ResponseEntity.ok(
                service.getApprovedOrganizations());
    }

    @Operation(summary = "Get rejected organizations")
    @GetMapping("/rejected")
    public ResponseEntity<List<Organization>>
            getRejectedOrganizations() {

        return ResponseEntity.ok(
                service.getRejectedOrganizations());
    }

    @Operation(summary = "Get organization by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Organization> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }
    

    @Operation(summary = "Approve or reject organization")
    @PutMapping("/{id}/approval")
    public ResponseEntity<Organization> processApproval(
            @PathVariable Long id,
            @RequestBody OrganizationApprovalRequest request) {

        return ResponseEntity.ok(
                service.processApproval(id, request));
    }
    

    @Operation(summary = "Update organization")
    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(
            @PathVariable Long id,
            @RequestBody OrganizationRegistrationRequest request) {

        return ResponseEntity.ok(
                service.updateOrganization(id, request));
    }

    @Operation(summary = "Delete organization")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(
            @PathVariable Long id) {

        service.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}