package com.kubis.microservices.rooms.controller;


import com.kubis.microservices.rooms.model.RoomModel;
import com.kubis.microservices.rooms.request.RoomRequest;
import com.kubis.microservices.rooms.response.RoomResponse;
import com.kubis.microservices.rooms.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
public class RoomController{
private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> addRoom(@RequestBody RoomRequest request){

        Long roomId = roomService.addRoom(request);
        log.info("New room with id: " + roomId + " was added");

        return new ResponseEntity<>(new RoomResponse(roomId, request.getMaxPeopleNumber(),
                request.getPrice(), request.getAvailableBeds(), request.getIsBathroomPrivate(),
                request.getAdditionalAmenities()), HttpStatus.CREATED);
    }

//    @GetMapping("/rooms")
//    public ResponseEntity<CollectionModel<EntityModel<RoomModel>>> getRooms(){
//
//        List<RoomModel> rooms = roomService.getRooms();
//
//        List<EntityModel<RoomModel>> roomModels = rooms.stream()
//                .map(room -> {
//                    Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoomController.class).getRooms())
//                            .slash(room.getId())
//                            .withSelfRel();
//
//                    String modifiedUri = selfLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");
//
//                    Link modifiedLink = Link.of(modifiedUri, selfLink.getRel());
//
//                    return EntityModel.of(room, modifiedLink);
//
//                }).toList();
//
//        Link collectionLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoomController.class).getRooms())
//                .withRel("All-rooms");
//
//        String modifiedUri = collectionLink.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");
//        Link modifiedLink = Link.of(modifiedUri, collectionLink.getRel());
//
//        return new ResponseEntity<>(CollectionModel.of(roomModels,modifiedLink), HttpStatus.OK);
//    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomModel>> getRooms(){
        return new ResponseEntity<>(roomService.getRooms(), HttpStatus.OK);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<EntityModel<RoomModel>> getRoomById(@PathVariable("id") long roomId){

        Optional<RoomModel> room = roomService.getRoomById(roomId);

        if(room.isPresent()) {
            Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoomController.class).getRoomById(roomId))
                    .withSelfRel();

            String modifiedUri = link.getHref().replaceFirst("http://\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+", "http://localhost:9091");

            Link modifiedLink = Link.of(modifiedUri, link.getRel());

            return new ResponseEntity<>(EntityModel.of(room.get(),modifiedLink), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>("Successfully Deleted Room with id: " + roomId, HttpStatus.OK);
    }

    @PatchMapping("/rooms/{id}")
    public ResponseEntity<Optional<RoomModel>> updateRoom(@PathVariable("id") Long roomId, @RequestBody RoomRequest request){
        return new ResponseEntity<>(roomService.updateRoom(roomId, request), HttpStatus.OK);
    }



}
