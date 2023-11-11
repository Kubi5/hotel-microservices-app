package com.kubis.microservices.reservations.clients;

import com.kubis.microservices.reservations.response.RoomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-rooms")
public interface RoomClient {

    @GetMapping("/rooms/{id}")
    public RoomResponse getRoomById(@PathVariable("id") Long id);
}