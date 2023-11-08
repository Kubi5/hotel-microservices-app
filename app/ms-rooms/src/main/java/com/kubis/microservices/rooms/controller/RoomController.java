package com.kubis.microservices.rooms.controller;


import com.kubis.microservices.rooms.model.RoomModel;
import com.kubis.microservices.rooms.request.RoomRequest;
import com.kubis.microservices.rooms.response.RoomResponse;
import com.kubis.microservices.rooms.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
public class RoomController {
private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> addRoom(@RequestBody RoomRequest request){

        Long roomId = roomService.addRoom(request);
        log.info("New room with id: " + roomId + " was added");

        return new ResponseEntity<>(new RoomResponse(roomId, request.getMaxPeopleNumber(),
                request.getPrice(), request.getAvailableBeds(), request.getIsBathroomPrivate(),
                request.getAdditionalAmenities()), HttpStatus.CREATED);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomModel>> getRooms(){
        return new ResponseEntity<>(roomService.getRooms(), HttpStatus.OK);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<Optional<RoomModel>> getRoomById(@PathVariable("id") Long roomId){
        return new ResponseEntity<>(roomService.getRoomById(roomId), HttpStatus.OK);
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
