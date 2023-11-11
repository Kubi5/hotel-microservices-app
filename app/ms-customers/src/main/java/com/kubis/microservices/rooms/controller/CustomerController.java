package com.kubis.microservices.rooms.controller;


import com.kubis.microservices.rooms.model.CustomerModel;
import com.kubis.microservices.rooms.request.CustomerRequest;
import com.kubis.microservices.rooms.request.LoginRequest;
import com.kubis.microservices.rooms.response.CustomerResponse;
import com.kubis.microservices.rooms.service.CustomerService;
import com.kubis.microservices.rooms.utils.JwtUtil;
import com.kubis.microservices.rooms.utils.Security;
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
private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> registerCustomer(@RequestBody CustomerRequest request){
        Long customerId = customerService.addCustomer(request);
        log.info("New customer with email: '" + request.getEmail() + "' was added");

        return new ResponseEntity<>(new CustomerResponse(customerId, request.getEmail(),
                Security.hashPassword(request.getPassword()), request.getFirstName(), request.getLastName(), request.getPhoneNumber(),
                request.getRole()), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> verifyLoginData(@RequestBody LoginRequest request){
        if(customerService.customerDataCorrect(request.getEmail(), request.getPassword())){
            String token = jwtUtil.generateToken(request.getEmail());
            return new ResponseEntity<>(token, HttpStatus.CREATED);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerModel>> getCustomers(){
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.OK);
    }

    @GetMapping("/customers/{email}")
    public ResponseEntity<CustomerModel> getCustomerByEmail(@PathVariable("email") String email){
        return new ResponseEntity<>(customerService.getCustomerByEmail(email),HttpStatus.OK);
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
