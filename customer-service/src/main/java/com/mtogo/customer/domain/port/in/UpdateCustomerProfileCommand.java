package com.mtogo.customer.domain.port.in;

public record UpdateCustomerProfileCommand(
        String fullName,
        String phone,
        String street,
        String city,
        String postalCode
) {}
