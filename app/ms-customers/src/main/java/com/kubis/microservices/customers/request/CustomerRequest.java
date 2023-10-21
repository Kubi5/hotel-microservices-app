package com.kubis.microservices.customers.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
