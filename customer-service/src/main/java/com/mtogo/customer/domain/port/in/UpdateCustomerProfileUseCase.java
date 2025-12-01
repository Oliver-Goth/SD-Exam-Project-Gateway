package com.mtogo.customer.domain.port.in;

import com.mtogo.customer.domain.model.Customer;

public interface UpdateCustomerProfileUseCase {
    Customer updateProfile(Long customerId, UpdateCustomerProfileCommand command);
}
