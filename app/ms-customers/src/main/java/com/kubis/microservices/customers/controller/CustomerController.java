package com.kubis.microservices.customers.controller;


import com.kubis.microservices.customers.model.CustomerModel;
import com.kubis.microservices.customers.request.CustomerRequest;
import com.kubis.microservices.customers.response.CustomerResponse;
import com.kubis.microservices.customers.service.CustomerService;
import com.kubis.microservices.customers.utils.Security;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
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

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerModel>> getCustomers(){
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Optional<CustomerModel>> getCustomerById(@PathVariable("id") Long customerId){
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long customerId){
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>("Successfully Deleted Customer with id: " + customerId, HttpStatus.OK);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Optional<CustomerModel>> updateCustomer(@PathVariable("id") Long customerId, @RequestBody CustomerRequest request){
        return new ResponseEntity<>(customerService.updateCustomer(customerId, request), HttpStatus.OK);
    }


}
