package com.kubis.microservices.rooms.service;

import com.kubis.microservices.rooms.model.RoomModel;
import com.kubis.microservices.rooms.repository.RoomRepository;
import com.kubis.microservices.rooms.request.RoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Long addRoom(RoomRequest request){
        RoomModel roomModel = RoomModel.builder()
                .maxPeopleNumber(request.getMaxPeopleNumber())
                .price(request.getPrice())
                .availableBeds(request.getAvailableBeds())
                .isBathroomPrivate(request.getIsBathroomPrivate())
                .additionalAmenities(request.getAdditionalAmenities())
                .build();
        return roomRepository.save(roomModel).getId();
    }

    public List<RoomModel> getRooms(){
        return roomRepository.findAll();
    }

    public Optional<RoomModel> getRoomById(Long id){
        return roomRepository.findById(id);
    }

    public void deleteRoom(Long id){
        roomRepository.deleteById(id);
    }

    public Optional<RoomModel> updateRoom(Long id, RoomRequest request){
        Optional<RoomModel> roomToUpdate = roomRepository.findById(id);

        if(roomToUpdate.isPresent()){
            if(request.getMaxPeopleNumber() != 0){
                roomToUpdate.get().setMaxPeopleNumber(request.getMaxPeopleNumber());
            }
            if(request.getPrice() != 0.0){
                roomToUpdate.get().setPrice(request.getPrice());
            }
            if(request.getAvailableBeds() != null){
                roomToUpdate.get().setAvailableBeds(request.getAvailableBeds());
            }
            if(request.getIsBathroomPrivate() != null) {
                roomToUpdate.get().setIsBathroomPrivate(request.getIsBathroomPrivate());
            }
            if(request.getAdditionalAmenities() != null){
                roomToUpdate.get().setAdditionalAmenities(request.getAdditionalAmenities());
            }

            roomRepository.save(roomToUpdate.get());
        }
        return roomToUpdate;
    }
}
