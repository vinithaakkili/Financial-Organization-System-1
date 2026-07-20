package com.example.demo.controller;

import com.example.demo.dto.OrganizationApprovalRequest;
import com.example.demo.dto.OrganizationRegistrationRequest;
import com.example.demo.entity.Organization;
import com.example.demo.entity.OrganizationStatus;
import com.example.demo.service.OrganizationService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.demo.exception.OrganizationNotFoundException;

@WebMvcTest(OrganizationController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService service;

    @Test
    void testGetAllOrganizations() throws Exception {

        Organization organization = new Organization();
        organization.setId(1L);
        organization.setOrganizationName("HDFC Bank");
        organization.setStatus(OrganizationStatus.APPROVED);

        when(service.getAll())
                .thenReturn(List.of(organization));

        mockMvc.perform(get("/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(
                        jsonPath("$[0].organizationName")
                                .value("HDFC Bank"))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("APPROVED"));
    }

    @Test
    void testGetAllOrganizationsEmptyList() throws Exception {

        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/organizations"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testRegisterOrganization() throws Exception {

        Organization savedOrganization =
                new Organization();

        savedOrganization.setId(6L);
        savedOrganization.setOrganizationName(
                "ICICI Bank");
        savedOrganization.setRegistrationNumber(
                "ICICI-2026-001");
        savedOrganization.setStatus(
                OrganizationStatus.PENDING);

        when(service.registerOrganization(
                any(OrganizationRegistrationRequest.class)))
                .thenReturn(savedOrganization);

        mockMvc.perform(
                post("/organizations/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "organizationName": "ICICI Bank",
                              "registrationNumber": "ICICI-2026-001",
                              "organizationType": "Private Bank",
                              "email": "contact@icici.com",
                              "phone": "9876543210",
                              "address": "Bengaluru",
                              "representativeName": "Vinitha",
                              "representativeEmail": "vinitha@icici.com"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(
                        jsonPath("$.organizationName")
                                .value("ICICI Bank"))
                .andExpect(
                        jsonPath("$.status")
                                .value("PENDING"));
    }

    @Test
    void testGetPendingOrganizations() throws Exception {

        Organization organization = new Organization();
        organization.setId(7L);
        organization.setOrganizationName("Canara Bank");
        organization.setStatus(
                OrganizationStatus.PENDING);

        when(service.getPendingOrganizations())
                .thenReturn(List.of(organization));

        mockMvc.perform(get("/organizations/pending"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].organizationName")
                                .value("Canara Bank"))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("PENDING"));
    }

    @Test
    void testGetApprovedOrganizations() throws Exception {

        Organization organization = new Organization();
        organization.setId(2L);
        organization.setOrganizationName("Axis Bank");
        organization.setStatus(
                OrganizationStatus.APPROVED);

        when(service.getApprovedOrganizations())
                .thenReturn(List.of(organization));

        mockMvc.perform(get("/organizations/approved"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].organizationName")
                                .value("Axis Bank"))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("APPROVED"));
    }

    @Test
    void testApproveOrganization() throws Exception {

        Organization approvedOrganization =
                new Organization();

        approvedOrganization.setId(8L);
        approvedOrganization.setOrganizationName(
                "Kotak Mahindra Bank");
        approvedOrganization.setStatus(
                OrganizationStatus.APPROVED);

        when(service.processApproval(
                eq(8L),
                any(OrganizationApprovalRequest.class)))
                .thenReturn(approvedOrganization);

        mockMvc.perform(
                put("/organizations/8/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "decision": "APPROVE"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(
                        jsonPath("$.status")
                                .value("APPROVED"));
    }

    @Test
    void testApproveOrganizationNotFound()
            throws Exception {

        when(service.processApproval(
                eq(99L),
                any(OrganizationApprovalRequest.class)))
                .thenThrow(
                        new OrganizationNotFoundException(
                                "Organization not found with id: 99"
                        )
                );

        mockMvc.perform(
                put("/organizations/99/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "decision": "APPROVE"
                            }
                            """)
        )
        .andExpect(status().isNotFound())
        .andExpect(
                jsonPath("$.status").value(404)
        )
        .andExpect(
                jsonPath("$.error")
                        .value("Not Found")
        )
        .andExpect(
                jsonPath("$.message")
                        .value(
                            "Organization not found with id: 99"
                        )
        );
    }
    @Test
    void testApprovalBadRequest()
            throws Exception {

        when(service.processApproval(
                eq(10L),
                any(OrganizationApprovalRequest.class)))
                .thenThrow(
                        new IllegalArgumentException(
                                "Decision must be APPROVE or REJECT"
                        )
                );

        mockMvc.perform(
                put("/organizations/10/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "decision": "INVALID"
                            }
                            """)
        )
        .andExpect(status().isBadRequest())
        .andExpect(
                jsonPath("$.status").value(400)
        )
        .andExpect(
                jsonPath("$.message")
                        .value(
                            "Decision must be APPROVE or REJECT"
                        )
        );
    }
    
    @Test
    void testRejectOrganization() throws Exception {

        Organization rejectedOrganization =
                new Organization();

        rejectedOrganization.setId(9L);
        rejectedOrganization.setOrganizationName(
                "Test Bank");
        rejectedOrganization.setStatus(
                OrganizationStatus.REJECTED);
        rejectedOrganization.setRejectionReason(
                "Invalid registration details");

        when(service.processApproval(
                eq(9L),
                any(OrganizationApprovalRequest.class)))
                .thenReturn(rejectedOrganization);

        mockMvc.perform(
                put("/organizations/9/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "decision": "REJECT",
                              "rejectionReason":
                                  "Invalid registration details"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("REJECTED"))
                .andExpect(
                        jsonPath("$.rejectionReason")
                                .value(
                                    "Invalid registration details"));
    }

    @Test
    void testRegisterOrganizationBadRequest()
            throws Exception {

        when(service.registerOrganization(
                any(OrganizationRegistrationRequest.class)))
                .thenThrow(
                        new IllegalArgumentException(
                                "Registration number already exists"
                        )
                );

        mockMvc.perform(
                post("/organizations/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "organizationName": "Axis Bank",
                              "registrationNumber": "AXIS-001"
                            }
                            """)
        )
        .andExpect(status().isBadRequest())
        .andExpect(
                jsonPath("$.status").value(400)
        )
        .andExpect(
                jsonPath("$.error")
                        .value("Bad Request")
        )
        .andExpect(
                jsonPath("$.message")
                        .value(
                            "Registration number already exists"
                        )
        );
    }
    
    @Test
    void testGetAllOrganizationsException()
            throws Exception {

        when(service.getAll())
                .thenThrow(
                        new RuntimeException(
                                "Database error"));

        mockMvc.perform(get("/organizations"))
                .andExpect(
                        status().isInternalServerError());
    }
}