package com.kubis.microservices.rooms.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RoomResponse {
    private long id;
    private int maxPeopleNumber;
    private double price;
    private List<String> availableBeds;
    private boolean isBathroomPrivate;
    private List<String> additionalAmenities;
}
