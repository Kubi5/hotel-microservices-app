package com.kubis.microservices.reservations.clients;

import com.kubis.microservices.reservations.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-customers")
public interface CustomerClient {
    @GetMapping("/customers/{email}")
    public CustomerResponse getCustomerByEmail(@PathVariable("email") String email);
}