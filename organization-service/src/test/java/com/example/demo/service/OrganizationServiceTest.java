package com.example.demo.service;

import com.example.demo.dto.OrganizationApprovalRequest;
import com.example.demo.dto.OrganizationRegistrationRequest;
import com.example.demo.entity.Organization;
import com.example.demo.entity.OrganizationStatus;
import com.example.demo.exception.OrganizationNotFoundException;
import com.example.demo.repository.OrganizationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository repository;

    @InjectMocks
    private OrganizationService service;

    @Test
    void testGetAllOrganizations() {

        Organization first = new Organization();
        Organization second = new Organization();

        when(repository.findAll())
                .thenReturn(List.of(first, second));

        List<Organization> result = service.getAll();

        assertEquals(2, result.size());

        verify(repository).findAll();
    }

    @Test
    void testGetAllOrganizationsEmpty() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<Organization> result = service.getAll();

        assertEquals(0, result.size());

        verify(repository).findAll();
    }

    @Test
    void testRegisterOrganization() {

        OrganizationRegistrationRequest request =
                createRegistrationRequest();

        when(repository.existsByRegistrationNumber(
                "AXIS-2026-001"))
                .thenReturn(false);

        when(repository.save(any(Organization.class)))
                .thenAnswer(invocation -> {

                    Organization organization =
                            invocation.getArgument(0);

                    organization.setId(1L);

                    return organization;
                });

        Organization result =
                service.registerOrganization(request);

        assertEquals(1L, result.getId());

        assertEquals(
                "Axis Bank",
                result.getOrganizationName()
        );

        assertEquals(
                "AXIS-2026-001",
                result.getRegistrationNumber()
        );

        assertEquals(
                OrganizationStatus.PENDING,
                result.getStatus()
        );

        verify(repository)
                .existsByRegistrationNumber(
                        "AXIS-2026-001"
                );

        verify(repository)
                .save(any(Organization.class));
    }

    @Test
    void testRegisterOrganizationDuplicateNumber() {

        OrganizationRegistrationRequest request =
                createRegistrationRequest();

        when(repository.existsByRegistrationNumber(
                "AXIS-2026-001"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.registerOrganization(request)
                );

        assertEquals(
                "Registration number already exists",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testGetPendingOrganizations() {

        Organization organization = new Organization();

        organization.setStatus(
                OrganizationStatus.PENDING
        );

        when(repository.findByStatus(
                OrganizationStatus.PENDING))
                .thenReturn(List.of(organization));

        List<Organization> result =
                service.getPendingOrganizations();

        assertEquals(1, result.size());

        assertEquals(
                OrganizationStatus.PENDING,
                result.get(0).getStatus()
        );

        verify(repository)
                .findByStatus(
                        OrganizationStatus.PENDING
                );
    }

    @Test
    void testGetPendingOrganizationsEmpty() {

        when(repository.findByStatus(
                OrganizationStatus.PENDING))
                .thenReturn(List.of());

        List<Organization> result =
                service.getPendingOrganizations();

        assertEquals(0, result.size());
    }

    @Test
    void testGetApprovedOrganizations() {

        Organization organization = new Organization();

        organization.setStatus(
                OrganizationStatus.APPROVED
        );

        when(repository.findByStatus(
                OrganizationStatus.APPROVED))
                .thenReturn(List.of(organization));

        List<Organization> result =
                service.getApprovedOrganizations();

        assertEquals(1, result.size());

        assertEquals(
                OrganizationStatus.APPROVED,
                result.get(0).getStatus()
        );

        verify(repository)
                .findByStatus(
                        OrganizationStatus.APPROVED
                );
    }

    @Test
    void testGetApprovedOrganizationsEmpty() {

        when(repository.findByStatus(
                OrganizationStatus.APPROVED))
                .thenReturn(List.of());

        List<Organization> result =
                service.getApprovedOrganizations();

        assertEquals(0, result.size());
    }

    @Test
    void testApproveOrganization() {

        Organization organization = new Organization();

        organization.setId(1L);
        organization.setStatus(
                OrganizationStatus.PENDING
        );

        organization.setRejectionReason(
                "Old rejection reason"
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("APPROVE");

        when(repository.findById(1L))
                .thenReturn(Optional.of(organization));

        when(repository.save(organization))
                .thenReturn(organization);

        Organization result =
                service.processApproval(1L, request);

        assertEquals(
                OrganizationStatus.APPROVED,
                result.getStatus()
        );

        assertNull(result.getRejectionReason());

        verify(repository).save(organization);
    }

    @Test
    void testRejectOrganization() {

        Organization organization = new Organization();

        organization.setId(2L);
        organization.setStatus(
                OrganizationStatus.PENDING
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("REJECT");

        request.setRejectionReason(
                "Registration document is invalid"
        );

        when(repository.findById(2L))
                .thenReturn(Optional.of(organization));

        when(repository.save(organization))
                .thenReturn(organization);

        Organization result =
                service.processApproval(2L, request);

        assertEquals(
                OrganizationStatus.REJECTED,
                result.getStatus()
        );

        assertEquals(
                "Registration document is invalid",
                result.getRejectionReason()
        );

        verify(repository).save(organization);
    }

    @Test
    void testRejectWithoutReason() {

        Organization organization = new Organization();

        organization.setId(3L);
        organization.setStatus(
                OrganizationStatus.PENDING
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("REJECT");
        request.setRejectionReason("");

        when(repository.findById(3L))
                .thenReturn(Optional.of(organization));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.processApproval(3L, request)
                );

        assertEquals(
                "Rejection reason is required",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testRejectWithNullReason() {

        Organization organization = new Organization();

        organization.setId(4L);
        organization.setStatus(
                OrganizationStatus.PENDING
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("REJECT");
        request.setRejectionReason(null);

        when(repository.findById(4L))
                .thenReturn(Optional.of(organization));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.processApproval(4L, request)
                );

        assertEquals(
                "Rejection reason is required",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testRejectWithBlankReason() {

        Organization organization = new Organization();

        organization.setId(5L);
        organization.setStatus(
                OrganizationStatus.PENDING
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("REJECT");
        request.setRejectionReason("   ");

        when(repository.findById(5L))
                .thenReturn(Optional.of(organization));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.processApproval(5L, request)
                );

        assertEquals(
                "Rejection reason is required",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testCannotProcessAlreadyApprovedOrganization() {

        Organization organization = new Organization();

        organization.setId(6L);
        organization.setStatus(
                OrganizationStatus.APPROVED
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("APPROVE");

        when(repository.findById(6L))
                .thenReturn(Optional.of(organization));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.processApproval(6L, request)
                );

        assertEquals(
                "Organization registration is already processed",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testCannotProcessAlreadyRejectedOrganization() {

        Organization organization = new Organization();

        organization.setId(7L);
        organization.setStatus(
                OrganizationStatus.REJECTED
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("REJECT");
        request.setRejectionReason("Invalid");

        when(repository.findById(7L))
                .thenReturn(Optional.of(organization));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.processApproval(7L, request)
                );

        assertEquals(
                "Organization registration is already processed",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testProcessApprovalOrganizationNotFound() {

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("APPROVE");

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrganizationNotFoundException.class,
                () -> service.processApproval(99L, request)
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testProcessApprovalInvalidDecision() {

        Organization organization = new Organization();

        organization.setId(8L);
        organization.setStatus(
                OrganizationStatus.PENDING
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision("INVALID");

        when(repository.findById(8L))
                .thenReturn(Optional.of(organization));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.processApproval(8L, request)
                );

        assertEquals(
                "Decision must be APPROVE or REJECT",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    @Test
    void testGetRejectedOrganizations() {

        Organization organization = new Organization();
        organization.setStatus(OrganizationStatus.REJECTED);

        when(repository.findByStatus(
                OrganizationStatus.REJECTED))
                .thenReturn(List.of(organization));

        List<Organization> result =
                service.getRejectedOrganizations();

        assertEquals(1, result.size());

        assertEquals(
                OrganizationStatus.REJECTED,
                result.get(0).getStatus()
        );

        verify(repository).findByStatus(
                OrganizationStatus.REJECTED
        );
    }
    @Test
    void testGetRejectedOrganizationsEmpty() {

        when(repository.findByStatus(
                OrganizationStatus.REJECTED))
                .thenReturn(List.of());

        List<Organization> result =
                service.getRejectedOrganizations();

        assertEquals(0, result.size());

        verify(repository).findByStatus(
                OrganizationStatus.REJECTED
        );
    }
    @Test
    void testUpdateOrganization() {

        Organization organization = new Organization();
        organization.setId(10L);
        organization.setStatus(OrganizationStatus.APPROVED);
        organization.setRejectionReason(null);

        OrganizationRegistrationRequest request =
                new OrganizationRegistrationRequest();

        when(repository.findById(10L))
                .thenReturn(Optional.of(organization));

        when(repository.save(organization))
                .thenReturn(organization);

        Organization result =
                service.updateOrganization(10L, request);

        assertEquals(10L, result.getId());

        assertEquals(
                OrganizationStatus.PENDING,
                result.getStatus()
        );

        assertNull(result.getRejectionReason());

        verify(repository).findById(10L);
        verify(repository).save(organization);
    }
    
    @Test
    void testProcessApprovalNullDecision() {

        Organization organization = new Organization();

        organization.setId(9L);
        organization.setStatus(
                OrganizationStatus.PENDING
        );

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision(null);

        when(repository.findById(9L))
                .thenReturn(Optional.of(organization));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.processApproval(9L, request)
                );

        assertEquals(
                "Decision must be APPROVE or REJECT",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(Organization.class));
    }

    private OrganizationRegistrationRequest
            createRegistrationRequest() {

        OrganizationRegistrationRequest request =
                new OrganizationRegistrationRequest();

        request.setOrganizationName("Axis Bank");

        request.setRegistrationNumber(
                "AXIS-2026-001"
        );

        return request;
    }
}