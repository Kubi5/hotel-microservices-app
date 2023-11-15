package com.kubis.microservices.reservations.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ReservationRequest {
    private long roomId;
    private int numberOfPeople;
    private String reservationStartDate;
    private String reservationEndDate;
}
