package com.kubis.microservices.reservations.response;

import lombok.Getter;



@Getter
public class CustomerResponse {
    private long customerId;
    private String email;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;

}