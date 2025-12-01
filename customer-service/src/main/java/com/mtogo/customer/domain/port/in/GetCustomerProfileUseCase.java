package com.mtogo.customer.domain.port.in;

import com.mtogo.customer.domain.model.Customer;

public interface GetCustomerProfileUseCase {
    Customer getProfile(Long customerId);
}
