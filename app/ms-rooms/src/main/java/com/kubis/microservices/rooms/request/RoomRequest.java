package com.kubis.microservices.rooms.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomRequest {
    private int maxPeopleNumber;
    private double price;
    private List<String> availableBeds;
    private boolean isBathroomPrivate;
    private List<String> additionalAmenities;
}
