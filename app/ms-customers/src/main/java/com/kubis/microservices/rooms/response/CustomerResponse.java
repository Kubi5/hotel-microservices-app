package com.kubis.microservices.rooms.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomerResponse {
    private long id;
    private String email;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private String phoneNumber;

}
