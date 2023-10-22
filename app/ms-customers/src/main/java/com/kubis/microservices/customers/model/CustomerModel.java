package com.kubis.microservices.customers.model;

import com.kubis.microservices.customers.utils.Security;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.Size;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMERS", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Pattern(regexp = Security.EMAIL_REGEX, message = "Incorrect email! Try again!")
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


}
