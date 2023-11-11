package com.kubis.microservices.reservations.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ReservationResponse {
    private long id;
    private long customerId;
    private long roomId;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationConfirmedDate;
    private int numberOfPeople;
    private double priceOfReservation;
}
