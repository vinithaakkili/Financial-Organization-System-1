package com.example.demo.controller;

import com.example.demo.entity.Organization;
import com.example.demo.service.OrganizationService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizationController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ disables security
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService service;

    @Test
    void testGetAllOrganizations() throws Exception {

        Organization org = new Organization();
        org.setOrganizationName("HDFC Bank");

        when(service.getAll()).thenReturn(List.of(org));

        mockMvc.perform(get("/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizationName").value("HDFC Bank"));
    }

    @Test
    void testGetAllOrganizations_EmptyList() throws Exception {

        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/organizations"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    void testAddOrganization() throws Exception {

        Organization org = new Organization();
        org.setId(1L);
        org.setOrganizationName("ICICI");

        when(service.save(any(Organization.class)))
        .thenReturn(org);

        mockMvc.perform(post("/organizations")
                .contentType("application/json")
                .content("""
                    {
                        "id": 1,
                        "organizationName": "ICICI"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationName").value("ICICI"));
    }
    @Test
    void testGetAllOrganizations_Exception() throws Exception {

        when(service.getAll()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/organizations"))
                .andExpect(status().isInternalServerError());
    }

}