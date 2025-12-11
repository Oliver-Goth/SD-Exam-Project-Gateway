package com.mtogo.customer.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class CustomerJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private String accountStatus;
    private String verificationStatus;
    private String verificationToken;

    private String phone;
    private String street;
    private String city;
    private String postalCode;

    public CustomerJpaEntity() {}

    // Constructor for saving
    public CustomerJpaEntity(Long id, String fullName, String email, String password,
                             String accountStatus, String verificationStatus, String verificationToken,
                             String phone, String street, String city, String postalCode) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.accountStatus = accountStatus;
        this.verificationStatus = verificationStatus;
        this.verificationToken = verificationToken;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getAccountStatus() { return accountStatus; }
    public String getVerificationStatus() { return verificationStatus; }
    public String getVerificationToken() { return verificationToken; }
    public String getPhone() { return phone; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }

    public void setId(Long id) { this.id = id; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
}
