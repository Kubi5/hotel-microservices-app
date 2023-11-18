package com.kubis.microservices.customers.model;

import com.kubis.microservices.customers.utils.Security;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMERS", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerId;
    @NotNull
    @Pattern(regexp = Security.EMAIL_REGEX, message = "Entered email is in the wrong format")
    private String email;
    @Size(min = 5)
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phoneNumber;

    private String role;


}
