package com.example.demo.service;

import com.example.demo.entity.Organization;
import com.example.demo.repository.OrganizationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository repository;

    @InjectMocks
    private OrganizationService service;

    @Test
    void testGetAllOrganizations() {
        when(repository.findAll()).thenReturn(List.of(new Organization(), new Organization()));
        assertEquals(2, service.getAll().size());
    }

    @Test
    void testGetAllOrganizations_Empty() {
        when(repository.findAll()).thenReturn(List.of());
        assertEquals(0, service.getAll().size());
    }

    @Test
    void testSaveOrganization() {
        Organization org = new Organization();
        org.setOrganizationName("Axis");

        when(repository.save(org)).thenReturn(org);

        Organization saved = service.save(org);

        assertEquals("Axis", saved.getOrganizationName());
    }
}