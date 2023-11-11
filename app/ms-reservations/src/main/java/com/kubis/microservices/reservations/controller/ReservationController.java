package com.kubis.microservices.reservations.controller;


import com.kubis.microservices.reservations.request.ReservationRequest;
import com.kubis.microservices.reservations.response.ReservationResponse;
import com.kubis.microservices.reservations.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ReservationController {

private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@RequestHeader("Authorization") String token, @RequestBody ReservationRequest request) throws ParseException {
        ReservationResponse response = reservationService.addReservation(token, request);

        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }




}
