package com.kubis.microservices.reservations.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SearchRequest {
    private int numberOfPeople;
    private double minPossiblePricePerDay;  //Room Table
    private double maxPossiblePricePerDay;   //Room Table
    private String reservationStartDate;  //Reservation Table
    private String reservationEndDate;    //Reservation Table
    private Boolean isBathroomPrivate;    //Room Table
    private List<String> selectedBeds;           //Room Table
    private List<String> selectedAdditionalAmenities;   //Room Table
}
