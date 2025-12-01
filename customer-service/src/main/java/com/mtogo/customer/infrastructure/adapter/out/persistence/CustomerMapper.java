package com.mtogo.customer.infrastructure.adapter.out.persistence;

import com.mtogo.customer.domain.model.AccountStatus;
import com.mtogo.customer.domain.model.Address;
import com.mtogo.customer.domain.model.ContactInfo;
import com.mtogo.customer.domain.model.Customer;
import com.mtogo.customer.domain.model.VerificationStatus;
import com.mtogo.customer.infrastructure.adapter.out.persistence.entity.CustomerJpaEntity;

import java.util.Collections;

public class CustomerMapper {

    public static Customer mapToDomain(CustomerJpaEntity entity) {
        Customer customer = new Customer(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPassword(),
                AccountStatus.valueOf(entity.getAccountStatus()),
                VerificationStatus.valueOf(entity.getVerificationStatus()),
                new ContactInfo(entity.getEmail(), entity.getPhone()),
                new Address(entity.getStreet(), entity.getCity(), entity.getPostalCode()),
                Collections.emptyList()
        );
        customer.setVerificationToken(entity.getVerificationToken());
        return customer;
    }

    public static CustomerJpaEntity mapToJpa(Customer domain) {
        return new CustomerJpaEntity(
                domain.getCustomerId(),
                domain.getFullName(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getAccountStatus().name(),
                domain.getVerificationStatus().name(),
                domain.getVerificationToken(),
                domain.getContactInfo().getPhone(),
                domain.getAddress().getStreet(),
                domain.getAddress().getCity(),
                domain.getAddress().getPostalCode()

        );
    }
}
