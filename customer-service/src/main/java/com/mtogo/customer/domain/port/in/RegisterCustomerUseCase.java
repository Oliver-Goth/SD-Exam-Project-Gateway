package com.mtogo.customer.domain.port.in;

import com.mtogo.customer.domain.model.Customer;

public interface RegisterCustomerUseCase {
    Customer registerCustomer(RegisterCustomerCommand command);
}
