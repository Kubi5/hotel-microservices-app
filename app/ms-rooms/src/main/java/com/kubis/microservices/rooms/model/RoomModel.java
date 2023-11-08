package com.kubis.microservices.rooms.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ROOMS")
public class RoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private int maxPeopleNumber;
    @NotNull
    private double price;
    @NotNull
    @ElementCollection
    private List<String> availableBeds;
    @NotNull
    private Boolean isBathroomPrivate;
    @ElementCollection
    private List<String> additionalAmenities;


}
