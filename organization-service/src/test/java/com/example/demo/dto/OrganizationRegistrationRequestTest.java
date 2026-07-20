package com.example.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class OrganizationRegistrationRequestTest {

    @Test
    void testGettersAndSetters() {

        OrganizationRegistrationRequest request =
                new OrganizationRegistrationRequest();

        request.setOrganizationName(null);
        request.setRegistrationNumber(null);
        request.setOrganizationType(null);
        request.setEmail(null);
        request.setPhone(null);
        request.setAddress(null);
        request.setRepresentativeName(null);
        request.setRepresentativeEmail(null);

        assertNull(request.getOrganizationName());
        assertNull(request.getRegistrationNumber());
        assertNull(request.getOrganizationType());
        assertNull(request.getEmail());
        assertNull(request.getPhone());
        assertNull(request.getAddress());
        assertNull(request.getRepresentativeName());
        assertNull(request.getRepresentativeEmail());
    }
}