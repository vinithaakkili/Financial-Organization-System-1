package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.OrganizationApprovalRequest;
import com.example.demo.dto.OrganizationRegistrationRequest;
import com.example.demo.entity.Organization;
import com.example.demo.entity.OrganizationStatus;
import com.example.demo.exception.OrganizationNotFoundException;
import com.example.demo.repository.OrganizationRepository;

@Service
public class OrganizationService {

	private final OrganizationRepository repository;

	public OrganizationService(OrganizationRepository repository) {
	    this.repository = repository;
	}

    public Organization registerOrganization(
            OrganizationRegistrationRequest request) {

        if (repository.existsByRegistrationNumber(
                request.getRegistrationNumber())) {

            throw new IllegalArgumentException(
                    "Registration number already exists");
        }

        Organization organization = new Organization();

        organization.setOrganizationName(
                request.getOrganizationName());

        organization.setRegistrationNumber(
                request.getRegistrationNumber());

        organization.setOrganizationType(
                request.getOrganizationType());

        organization.setEmail(request.getEmail());
        organization.setPhone(request.getPhone());
        organization.setAddress(request.getAddress());

        organization.setRepresentativeName(
                request.getRepresentativeName());

        organization.setRepresentativeEmail(
                request.getRepresentativeEmail());

        /*
         * Status is always assigned by the backend.
         * The organization representative cannot approve
         * their own organization.
         */
        organization.setStatus(OrganizationStatus.PENDING);

        return repository.save(organization);
    }

    public List<Organization> getAll() {
        return repository.findAll();
    }

    public List<Organization> getPendingOrganizations() {
        return repository.findByStatus(
                OrganizationStatus.PENDING);
    }

    public List<Organization> getApprovedOrganizations() {
        return repository.findByStatus(
                OrganizationStatus.APPROVED);
    }

    public List<Organization> getRejectedOrganizations() {
        return repository.findByStatus(
                OrganizationStatus.REJECTED);
    }

    public Organization getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with id: " + id));
    }

    public Organization processApproval(
            Long organizationId,
            OrganizationApprovalRequest request) {

        Organization organization = getById(organizationId);

        if (organization.getStatus() != OrganizationStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Organization registration is already processed");
        }

        if ("APPROVE".equalsIgnoreCase(request.getDecision())) {

            organization.setStatus(
                    OrganizationStatus.APPROVED);

            organization.setRejectionReason(null);

        } else if ("REJECT".equalsIgnoreCase(
                request.getDecision())) {

            if (request.getRejectionReason() == null
                    || request.getRejectionReason().isBlank()) {

                throw new IllegalArgumentException(
                        "Rejection reason is required");
            }

            organization.setStatus(
                    OrganizationStatus.REJECTED);

            organization.setRejectionReason(
                    request.getRejectionReason());

        } else {
            throw new IllegalArgumentException(
                    "Decision must be APPROVE or REJECT");
        }

        return repository.save(organization);
    }

    public Organization updateOrganization(
            Long id,
            OrganizationRegistrationRequest request) {

        Organization organization = getById(id);

        organization.setOrganizationName(
                request.getOrganizationName());

        organization.setRegistrationNumber(
                request.getRegistrationNumber());

        organization.setOrganizationType(
                request.getOrganizationType());

        organization.setEmail(request.getEmail());
        organization.setPhone(request.getPhone());
        organization.setAddress(request.getAddress());

        organization.setRepresentativeName(
                request.getRepresentativeName());

        organization.setRepresentativeEmail(
                request.getRepresentativeEmail());

        /*
         * When an organization edits its information,
         * finance must approve it again.
         */
        organization.setStatus(OrganizationStatus.PENDING);
        organization.setRejectionReason(null);

        return repository.save(organization);
    }

    public void deleteOrganization(Long id) {
        Organization organization = getById(id);
        repository.delete(organization);
    }
}