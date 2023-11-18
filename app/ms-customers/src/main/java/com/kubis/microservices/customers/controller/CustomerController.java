package com.kubis.microservices.customers.controller;


import com.kubis.microservices.customers.model.CustomerModel;
import com.kubis.microservices.customers.request.CustomerRequest;
import com.kubis.microservices.customers.response.CustomerResponse;
import com.kubis.microservices.customers.service.CustomerService;
import com.kubis.microservices.customers.utils.JwtUtil;
import com.kubis.microservices.customers.request.LoginRequest;
import com.kubis.microservices.customers.utils.Security;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
            return new ResponseEntity<>(token, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<CustomerModel>>> getCustomers(@RequestHeader("Authorization") String token){
        if(customerService.senderIsAdmin(token)) {
            List<CustomerModel> customers = customerService.getCustomers();

            List<EntityModel<CustomerModel>> customersModels = customers.stream()
                    .map(customer -> {
                        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomers(token))
                                .slash(customer.getEmail())
                                .withSelfRel();

                        String modifiedUri = selfLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");

                        Link modifiedLink = Link.of(modifiedUri, selfLink.getRel());

                        return EntityModel.of(customer, modifiedLink);

                    }).toList();

            Link collectionLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomers(token))
                    .withRel("All-customers");

            String modifiedUri = collectionLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");
            Link modifiedLink = Link.of(modifiedUri, collectionLink.getRel());

            return new ResponseEntity<>(CollectionModel.of(customersModels, modifiedLink), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/customers/{email}")
    public ResponseEntity<EntityModel<CustomerModel>> getCustomerByEmail(@PathVariable("email") String email, @RequestHeader("Authorization") String token){
        if(customerService.senderIsAdmin(token) || customerService.senderIsUserWithPermissions(token, email)) {
            Optional<CustomerModel> customer = customerService.getCustomerByEmail(email);

            if (customer.isPresent()) {
                Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerByEmail(email, token))
                        .withSelfRel();

                String modifiedUri = link.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");

                Link modifiedLink = Link.of(modifiedUri, link.getRel());

                return new ResponseEntity<>(EntityModel.of(customer.get(), modifiedLink), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long customerId,@RequestHeader("Authorization") String token){
        if(customerService.senderIsAdmin(token)) {
            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>("Successfully Deleted Customer with id: " + customerId, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Optional<CustomerModel>> updateCustomer(@PathVariable("id") Long customerId, @RequestBody CustomerRequest request, @RequestHeader("Authorization") String token){
        if(customerService.senderIsAdmin(token)) {
            return new ResponseEntity<>(customerService.updateCustomer(customerId, request), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }



}
