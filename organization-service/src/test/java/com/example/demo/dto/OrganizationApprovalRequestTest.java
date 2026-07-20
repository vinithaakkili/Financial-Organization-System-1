package com.example.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class OrganizationApprovalRequestTest {

    @Test
    void testGettersAndSetters() {

        OrganizationApprovalRequest request =
                new OrganizationApprovalRequest();

        request.setDecision(null);
        request.setRejectionReason(null);

        assertNull(request.getDecision());
        assertNull(request.getRejectionReason());
    }
}