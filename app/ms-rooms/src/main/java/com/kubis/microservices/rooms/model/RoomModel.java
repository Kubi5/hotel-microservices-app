package com.kubis.microservices.rooms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

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
    private List<String> availableBeds;
    private boolean isBathroomPrivate;

    private List<String> additionalAmenities;


}
