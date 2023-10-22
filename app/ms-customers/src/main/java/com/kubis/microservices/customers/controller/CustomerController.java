package com.kubis.microservices.customers.controller;


import com.kubis.microservices.customers.request.CustomerRequest;
import com.kubis.microservices.customers.response.CustomerResponse;
import com.kubis.microservices.customers.service.CustomerService;
import com.kubis.microservices.customers.utils.Security;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping
public class CustomerController {
private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> registerCustomer(@RequestBody CustomerRequest request){
        Long customerId = customerService.addCustomer(request);
        log.info("New customer with email: '" + request.getEmail() + "' was added");

        return new ResponseEntity<>(new CustomerResponse(customerId, request.getFirstName(),
                request.getLastName(), request.getPhoneNumber(), request.getEmail(), Security.hashPassword(request.getPassword())),
                HttpStatus.CREATED);
    }
}
