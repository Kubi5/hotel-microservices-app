package com.kubis.microservices.reservations.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RESERVATIONS")
public class ReservationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reservationId;
    @NotNull
    private long customerId;
    @NotNull
    private long roomId;
    @NotNull
    private LocalDate reservationStartDate;
    @NotNull
    private LocalDate reservationEndDate;
    @NotNull
    private Date reservationConfirmedDate;
    @NotNull
    private int numberOfPeople;
    @NotNull
    private double priceOfReservation;


}
