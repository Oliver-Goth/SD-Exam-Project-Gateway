package com.mtogo.restaurant.domain.model;

public class Restaurant {
    private Long restaurantId;
    private final String name;
    private final String ownerName;
    private final String email;
    private String password;
    private final String phone;
    private final String address;
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    private String operatingHours;

    public Restaurant(String name, String ownerName, String email, String password, String phone, String address) {
        this.name = name;
        this.ownerName = ownerName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void approve() {
        this.approvalStatus = ApprovalStatus.APPROVED;
    }

    public void suspend() {
        this.approvalStatus = ApprovalStatus.SUSPENDED;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
}
