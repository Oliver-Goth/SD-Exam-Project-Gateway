package com.mtogo.customer.domain.port.in;

public interface VerifyCustomerUseCase {
    boolean verifyCustomer(Long customerId, String token);

    void verify(String token);
}
