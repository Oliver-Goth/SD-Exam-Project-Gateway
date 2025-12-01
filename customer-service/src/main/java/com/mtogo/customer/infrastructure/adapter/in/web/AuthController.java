package com.mtogo.customer.infrastructure.adapter.in.web;

import com.mtogo.customer.domain.port.in.RegisterCustomerUseCase;
import com.mtogo.customer.domain.port.in.LoginUseCase;
import com.mtogo.customer.domain.port.in.VerifyCustomerUseCase;
import com.mtogo.customer.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.mtogo.customer.infrastructure.adapter.in.web.dto.LoginRequest;
import com.mtogo.customer.infrastructure.adapter.in.web.dto.TokenResponse;
import com.mtogo.customer.domain.port.in.RegisterCustomerCommand;
import com.mtogo.customer.domain.model.Customer;
import com.mtogo.customer.application.exception.InvalidCredentialsException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final LoginUseCase loginUseCase;
    private final VerifyCustomerUseCase verifyCustomerUseCase;

    // ⭐⭐ THIS CONSTRUCTOR IS REQUIRED ⭐⭐
    public AuthController(RegisterCustomerUseCase registerCustomerUseCase,
                          LoginUseCase loginUseCase,
                          VerifyCustomerUseCase verifyCustomerUseCase) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.loginUseCase = loginUseCase;
        this.verifyCustomerUseCase = verifyCustomerUseCase;
    }

    // ------------------------------------------
    // Register
    // ------------------------------------------
    @Operation(summary = "Register a new customer", description = "Creates account and sends verification email")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer register(@RequestBody RegisterRequest request) {

        RegisterCustomerCommand cmd = new RegisterCustomerCommand();
        cmd.fullName = request.fullName;
        cmd.email = request.email;
        cmd.rawPassword = request.password;
        cmd.phone = request.phone;
        cmd.street = request.street;
        cmd.city = request.city;
        cmd.postalCode = request.postalCode;

        return registerCustomerUseCase.registerCustomer(cmd);
    }

    // ------------------------------------------
    // Login
    // ------------------------------------------
    @Operation(summary = "Login", description = "Returns JWT token")
    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        String token = loginUseCase.login(request.getEmail(), request.getPassword());
        return new TokenResponse(token);
    }

    // ------------------------------------------
    // Verify
    // ------------------------------------------
    @GetMapping("/verify")
    public String verifyAccount(
            @RequestParam String token) {

        verifyCustomerUseCase.verify(token);

        return "Account verified successfully!";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleInvalidCredentials(InvalidCredentialsException ex) {
        return ex.getMessage();
    }
}
