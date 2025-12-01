package com.mtogo.customer.infrastructure.adapter.in.web;

import com.mtogo.customer.domain.model.Customer;
import com.mtogo.customer.domain.port.in.GetCustomerProfileUseCase;
import com.mtogo.customer.domain.port.in.UpdateCustomerProfileCommand;
import com.mtogo.customer.domain.port.in.UpdateCustomerProfileUseCase;
import com.mtogo.customer.infrastructure.adapter.in.web.dto.UpdateCustomerProfileRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final GetCustomerProfileUseCase getCustomerProfileUseCase;
    private final UpdateCustomerProfileUseCase updateCustomerProfileUseCase;

    public CustomerController(GetCustomerProfileUseCase getCustomerProfileUseCase,
                              UpdateCustomerProfileUseCase updateCustomerProfileUseCase) {
        this.getCustomerProfileUseCase = getCustomerProfileUseCase;
        this.updateCustomerProfileUseCase = updateCustomerProfileUseCase;
    }

    @GetMapping("/me")
    public Customer getMyProfile(Authentication auth) {

        Long userId = Long.parseLong(auth.getName()); // Extracted from JWT filter

        return getCustomerProfileUseCase.getProfile(userId);
    }

    @PutMapping("/me")
    public Customer updateMyProfile(
            Authentication auth,
            @RequestBody UpdateCustomerProfileRequest request
    ) {
        Long userId = Long.parseLong(auth.getName());

        UpdateCustomerProfileCommand command =
                new UpdateCustomerProfileCommand(
                        request.fullName(),
                        request.phone(),
                        request.street(),
                        request.city(),
                        request.postalCode()
                );

        return updateCustomerProfileUseCase.updateProfile(userId, command);
    }
}
