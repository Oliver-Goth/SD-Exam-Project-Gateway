package com.mtogo.customer.infrastructure.adapter.in.web.dto;

public record UpdateCustomerProfileRequest(
        String fullName,
        String phone,
        String street,
        String city,
        String postalCode
) {}
