package com.kubis.microservices.reservations.controller;


import com.kubis.microservices.reservations.model.ReservationModel;
import com.kubis.microservices.reservations.request.ReservationRequest;
import com.kubis.microservices.reservations.request.SearchRequest;
import com.kubis.microservices.reservations.response.ReservationResponse;
import com.kubis.microservices.reservations.response.RoomResponse;
import com.kubis.microservices.reservations.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ReservationController {

private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@RequestHeader("Authorization") String token, @RequestBody ReservationRequest request) {

            ReservationResponse response = reservationService.addReservation(token, request);

            if (response != null) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                return ResponseEntity.badRequest().build();
            }

    }

    @PostMapping("/reservations/search")
    public ResponseEntity<List<RoomResponse>> searchForRoomsThatMeetTheCriteria(@RequestBody SearchRequest request){

        return new ResponseEntity<>(reservationService.searchForRoomsWithCriteria(request), HttpStatus.OK);
    }

    @GetMapping("/reservations/me")
    public ResponseEntity<CollectionModel<EntityModel<ReservationModel>>> getReservationsByCustomerId(@RequestHeader("Authorization") String token){
        List<ReservationModel> reservations = reservationService.getReservationsByCustomerId(token);

        List<EntityModel<ReservationModel>> reservationModels = reservations.stream()
                .map(reservation -> {
                    Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getReservations(token))
                            .slash(reservation.getReservationId())
                            .withSelfRel();

                    String modifiedUri = selfLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");

                    Link modifiedLink = Link.of(modifiedUri, selfLink.getRel());

                    return EntityModel.of(reservation, modifiedLink);

                }).toList();

        Link collectionLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getReservations(token))
                .slash("me")
                .withRel("All-reservations");

        String modifiedUri = collectionLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");
        Link modifiedLink = Link.of(modifiedUri, collectionLink.getRel());

        return new ResponseEntity<>(CollectionModel.of(reservationModels,modifiedLink), HttpStatus.OK);
    }

    @GetMapping("/reservations")
    public ResponseEntity<CollectionModel<EntityModel<ReservationModel>>> getReservations(@RequestHeader("Authorization") String token){
        if(reservationService.senderIsAdmin(token)) {
            List<ReservationModel> reservations = reservationService.getReservations();

            List<EntityModel<ReservationModel>> reservationModels = reservations.stream()
                    .map(reservation -> {
                        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getReservations(token))
                                .slash(reservation.getReservationId())
                                .withSelfRel();

                        String modifiedUri = selfLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");

                        Link modifiedLink = Link.of(modifiedUri, selfLink.getRel());

                        return EntityModel.of(reservation, modifiedLink);

                    }).toList();

            Link collectionLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getReservations(token))
                    .withRel("All-reservations");

            String modifiedUri = collectionLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");
            Link modifiedLink = Link.of(modifiedUri, collectionLink.getRel());

            return new ResponseEntity<>(CollectionModel.of(reservationModels, modifiedLink), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<EntityModel<ReservationModel>> getReservationById(@PathVariable("id") long id, @RequestHeader("Authorization") String token){
        if(reservationService.senderIsAdmin(token)) {
            Optional<ReservationModel> reservation = reservationService.getReservationById(id);

            if (reservation.isPresent()) {
                Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getReservationById(id, token))
                        .withSelfRel();

                String modifiedUri = link.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");

                Link modifiedLink = Link.of(modifiedUri, link.getRel());

                return new ResponseEntity<>(EntityModel.of(reservation.get(), modifiedLink), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }





}
