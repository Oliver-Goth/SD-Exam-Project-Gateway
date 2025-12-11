package com.mtogo.customer.domain.model;

import java.util.List;

public class Customer {

    private Long customerId;
    private String fullName;
    private String email;
    private String password;
    private AccountStatus accountStatus;
    private VerificationStatus verificationStatus;
    private String verificationToken;
    private ContactInfo contactInfo;
    private Address address;
    private final List<PreviousOrder> previousOrders;

    public Customer(Long customerId,
                    String fullName,
                    String email,
                    String password,
                    AccountStatus accountStatus,
                    VerificationStatus verificationStatus,
                    ContactInfo contactInfo,
                    Address address,
                    List<PreviousOrder> previousOrders) {

        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.accountStatus = accountStatus;
        this.verificationStatus = verificationStatus;
        this.contactInfo = contactInfo;
        this.address = address;
        this.previousOrders = previousOrders == null ? List.of() : List.copyOf(previousOrders);
        this.verificationToken = null;
    }

    // --- Add this new domain behavior ---
    public void verifyAccount() {
        this.verificationStatus = VerificationStatus.VERIFIED;
    }

    public void updateFullName(String fullName) {
        this.fullName = fullName;
    }

    public void updateContactPhone(String phone) {
        if (this.contactInfo != null) {
            this.contactInfo = new ContactInfo(this.contactInfo.getEmail(), phone);
        }
    }

    public void updateAddress(String street, String city, String postalCode) {
        this.address = new Address(street, city, postalCode);
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String token) {
        this.verificationToken = token;
    }

    // --- Getters ---
    public Long getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public AccountStatus getAccountStatus() { return accountStatus; }
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public ContactInfo getContactInfo() { return contactInfo; }
    public Address getAddress() { return address; }
    public List<PreviousOrder> getPreviousOrders() { return previousOrders; }
}
