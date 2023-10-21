package com.kubis.microservices.customers.service;

import com.kubis.microservices.customers.model.CustomerModel;
import com.kubis.microservices.customers.repository.CustomerRepository;
import com.kubis.microservices.customers.request.CustomerRequest;
import com.kubis.microservices.customers.utils.Security;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Long addCustomer(CustomerRequest request){
        CustomerModel customerModel = CustomerModel.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(Security.hashPassword(request.getPassword()))
                .build();
        return customerRepository.save(customerModel).getId();
    }
}
