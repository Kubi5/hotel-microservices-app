package com.kubis.microservices.rooms.clients;

import com.kubis.microservices.rooms.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-customers")
public interface CustomerClient {
    @GetMapping("/customers/{email}")
    public CustomerResponse getCustomerByEmail(@PathVariable("email") String email, @RequestHeader("Authorization") String token);
}
